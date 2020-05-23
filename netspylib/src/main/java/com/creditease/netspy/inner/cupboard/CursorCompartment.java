package com.creditease.netspy.inner.cupboard;

import android.database.Cursor;

import com.creditease.netspy.inner.cupboard.convert.EntityConverter;

import java.util.List;

public class CursorCompartment extends com.creditease.netspy.inner.cupboard.BaseCompartment {

    private final Cursor mCursor;

    protected CursorCompartment(Cupboard cupboard, Cursor cursor) {
        super(cupboard);
        this.mCursor = cursor;
    }

    /**
     * Create a {link Iterable} of objects.
     *
     * @param clz the entity type
     * @return the iterable
     */
    public <T> QueryResultIterable<T> iterate(Class<T> clz) {
        EntityConverter<T> converter = getConverter(clz);
        return new QueryResultIterable<T>(mCursor, converter);
    }

    /**
     * Get the first entity from the cursor
     *
     * @param clz the entity type
     * @return the object or null if the cursor has no results.
     */
    public <T> T get(Class<T> clz) {
        return iterate(clz).get(false);
    }

    /**
     * Get the contents of this cursor as a list, starting at the current position.
     * Only to be used if the resultset is to be expected of reasonable size.
     *
     * @param clz the entity type
     * @return the list of entities obtained from the cursor.
     */
    public <T> List<T> list(Class<T> clz) {
        return iterate(clz).list(false);
    }
}
