package com.creditease.netspy.cupboard.internal.convert;

import android.content.ContentValues;
import android.database.Cursor;

import com.creditease.netspy.cupboard.Cupboard;
import com.creditease.netspy.cupboard.convert.EntityConverter;
import com.creditease.netspy.cupboard.convert.EntityConverter.ColumnType;
import com.creditease.netspy.cupboard.convert.EntityConverterFactory;
import com.creditease.netspy.cupboard.convert.FieldConverter;
import com.creditease.netspy.cupboard.convert.FieldConverterFactory;
import com.creditease.netspy.cupboard.convert.ReflectiveEntityConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Internal registry for converters, mostly inspired by Google Gson
 */
public class ConverterRegistry {

    private static final int DEFAULT_ENTITY_CONVERTER_COUNT = 1;
    private static final int DEFAULT_FIELD_CONVERTER_COUNT = 3;
    private final ThreadLocal<Map<Type, FutureFieldConverter<?>>> mFieldConverterCalls = new ThreadLocal<Map<Type, FutureFieldConverter<?>>>();
    private final ThreadLocal<Map<Class<?>, EntityConverter<?>>> mEntityConverterCalls = new ThreadLocal<Map<Class<?>, EntityConverter<?>>>();
    List<FieldConverterFactory> mFieldConverterFactories = new ArrayList<FieldConverterFactory>(256);
    List<EntityConverterFactory> mEntityConverterFactories = new ArrayList<EntityConverterFactory>(64);
    private Map<Class<?>, EntityConverter<?>> mEntityConverterCache = new HashMap<Class<?>, EntityConverter<?>>(128);
    private Map<Type, FieldConverter<?>> mFieldConverterCache = new HashMap<Type, FieldConverter<?>>(128);
    private Cupboard mCupboard;

    public ConverterRegistry(Cupboard cupboard) {
        this.mCupboard = cupboard;
        addDefaultEntityConverterFactories();
        addDefaultFieldConverterFactories();
    }

    public ConverterRegistry(ConverterRegistry source, Cupboard cupboard) {
        this.mCupboard = cupboard;
        mFieldConverterFactories.addAll(source.mFieldConverterFactories);
        mEntityConverterFactories.addAll(source.mEntityConverterFactories);
    }

    private void addDefaultFieldConverterFactories() {
        mFieldConverterFactories.add(new DefaultFieldConverterFactory());
        mFieldConverterFactories.add(new EnumFieldConverterFactory());
        mFieldConverterFactories.add(new EntityFieldConverterFactory());
    }

    private void addDefaultEntityConverterFactories() {
        mEntityConverterFactories.add(new EntityConverterFactory() {
            @Override
            public <T> EntityConverter<T> create(Cupboard cupboard, Class<T> type) {
                return new ReflectiveEntityConverter<T>(cupboard, type);
            }
        });
    }

    public <T> EntityConverter<T> getEntityConverter(Class<T> type) throws IllegalArgumentException {
        EntityConverter<?> cached = mEntityConverterCache.get(type);
        if (cached != null) {
            return (EntityConverter<T>) cached;
        }
        boolean requiresThreadLocalCleanup = false;
        Map<Class<?>, EntityConverter<?>> threadCalls = mEntityConverterCalls.get();
        if (threadCalls == null) {
            threadCalls = new HashMap<Class<?>, EntityConverter<?>>(16);
            mEntityConverterCalls.set(threadCalls);
            requiresThreadLocalCleanup = true;
        }
        // doesn't this leak a thread local in a race condition?
        FutureEntityConverter<T> ongoingCall = (FutureEntityConverter<T>) threadCalls.get(type);
        if (ongoingCall != null) {
            return ongoingCall;
        }

        try {
            FutureEntityConverter<T> call = new FutureEntityConverter<T>();
            threadCalls.put(type, call);

            for (EntityConverterFactory factory : mEntityConverterFactories) {
                EntityConverter<T> candidate = factory.create(mCupboard, type);
                if (candidate != null) {
                    call.setDelegate(candidate);
                    mEntityConverterCache.put(type, candidate);
                    return candidate;
                }
            }
            throw new IllegalArgumentException("Cannot convert entity of type " + type);
        } finally {
            threadCalls.remove(type);
            if (requiresThreadLocalCleanup) {
                mEntityConverterCalls.remove();
            }
        }
    }

    public <T> FieldConverter<T> getFieldConverter(Type type) throws IllegalArgumentException {
        FieldConverter<T> converter = (FieldConverter<T>) mFieldConverterCache.get(type);
        if (converter != null) {
            return converter;
        }
        boolean requiresThreadLocalCleanup = false;
        Map<Type, FutureFieldConverter<?>> threadCalls = mFieldConverterCalls.get();
        if (threadCalls == null) {
            threadCalls = new HashMap<Type, FutureFieldConverter<?>>(16);
            mFieldConverterCalls.set(threadCalls);
            requiresThreadLocalCleanup = true;
        }
        // doesn't this leak a thread local in a race condition?
        FutureFieldConverter<T> ongoingCall = (FutureFieldConverter<T>) threadCalls.get(type);
        if (ongoingCall != null) {
            Map<Class<?>, EntityConverter<?>> entityThreadCalls = mEntityConverterCalls.get();
            // prevent the case where an EntityConverter is being requested, that requests a FieldConverter for the same
            // EntityConverter, although it defeats the purpose of the ongoing call check.
            if (!(type instanceof Class && mCupboard.isRegisteredEntity((Class<?>) type) && entityThreadCalls.containsKey(type))) {
                return ongoingCall;
            }
        }

        try {
            FutureFieldConverter<T> call = new FutureFieldConverter<T>();
            threadCalls.put(type, call);

            for (FieldConverterFactory factory : mFieldConverterFactories) {
                FieldConverter<T> candidate = (FieldConverter<T>) factory.create(mCupboard, type);
                if (candidate != null) {
                    call.setDelegate(candidate);
                    mFieldConverterCache.put(type, candidate);
                    return candidate;
                }
            }
            throw new IllegalArgumentException("Cannot convert field of type" + type);
        } finally {
            threadCalls.remove(type);
            if (requiresThreadLocalCleanup) {
                mFieldConverterCalls.remove();
            }
        }
    }

    public <T> EntityConverter<T> getDelegateEntityConverter(EntityConverterFactory skipPast, Class<T> entityClass) throws IllegalArgumentException {
        boolean factoryFound = false;
        for (EntityConverterFactory factory : mEntityConverterFactories) {
            if (!factoryFound) {
                if (factory == skipPast) {
                    factoryFound = true;
                }
                continue;
            }
            EntityConverter<T> candidate = factory.create(mCupboard, entityClass);
            if (candidate != null) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Cannot convert entity of type " + entityClass);
    }

    public FieldConverter getDelegateFieldConverter(FieldConverterFactory skipPast, Type fieldType) throws IllegalArgumentException {
        boolean factoryFound = false;
        for (FieldConverterFactory factory : mFieldConverterFactories) {
            if (!factoryFound) {
                if (factory == skipPast) {
                    factoryFound = true;
                }
                continue;
            }
            FieldConverter candidate = factory.create(mCupboard, fieldType);
            if (candidate != null) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Cannot convert field of type " + fieldType);
    }

    public void registerEntityConverterFactory(EntityConverterFactory factory) {
        mEntityConverterFactories.add(mEntityConverterFactories.size() - DEFAULT_ENTITY_CONVERTER_COUNT, factory);
    }

    public void registerFieldConverterFactory(FieldConverterFactory factory) {
        mFieldConverterFactories.add(mFieldConverterFactories.size() - DEFAULT_FIELD_CONVERTER_COUNT, factory);
    }

    public <T> void registerFieldConverter(Class<T> clz, FieldConverter<T> converter) {
        mFieldConverterCache.put(clz, converter);
    }

    private static class FutureFieldConverter<T> implements FieldConverter<T> {
        private FieldConverter<T> mDelegate;

        @Override
        public T fromCursorValue(Cursor cursor, int columnIndex) {
            if (mDelegate == null) {
                throw new IllegalStateException();
            }
            return mDelegate.fromCursorValue(cursor, columnIndex);
        }

        @Override
        public void toContentValue(T value, String key, ContentValues values) {
            if (mDelegate == null) {
                throw new IllegalStateException();
            }
            mDelegate.toContentValue(value, key, values);
        }

        @Override
        public ColumnType getColumnType() {
            if (mDelegate == null) {
                throw new IllegalStateException();
            }
            return mDelegate.getColumnType();
        }

        void setDelegate(FieldConverter<T> delegate) {
            if (mDelegate != null) {
                throw new AssertionError();
            }
            mDelegate = delegate;
        }
    }

    private static class FutureEntityConverter<T> implements EntityConverter<T> {
        private EntityConverter<T> mDelegate;

        @Override
        public T fromCursor(Cursor cursor) {
            if (mDelegate == null) {
                throw new IllegalStateException();
            }
            return mDelegate.fromCursor(cursor);
        }

        @Override
        public void toValues(T object, ContentValues values) {
            if (mDelegate == null) {
                throw new IllegalStateException();
            }
            mDelegate.toValues(object, values);
        }

        @Override
        public List<Column> getColumns() {
            if (mDelegate == null) {
                throw new IllegalStateException();
            }
            return mDelegate.getColumns();
        }

        @Override
        public void setId(Long id, T instance) {
            if (mDelegate == null) {
                throw new IllegalStateException();
            }
            mDelegate.setId(id, instance);
        }

        @Override
        public Long getId(T instance) {
            if (mDelegate == null) {
                throw new IllegalStateException();
            }
            return mDelegate.getId(instance);
        }

        @Override
        public String getTable() {
            if (mDelegate == null) {
                throw new IllegalStateException();
            }
            return mDelegate.getTable();
        }

        void setDelegate(EntityConverter<T> delegate) {
            if (mDelegate != null) {
                throw new AssertionError();
            }
            mDelegate = delegate;
        }
    }
}
