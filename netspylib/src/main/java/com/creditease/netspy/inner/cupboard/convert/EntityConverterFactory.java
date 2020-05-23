package com.creditease.netspy.inner.cupboard.convert;

import com.creditease.netspy.inner.cupboard.Cupboard;

/**
 * An entity converter factory instantiates {@link EntityConverter}s. A single factory may support
 * multiple entity types.
 */
public interface EntityConverterFactory {
    /**
     * Create a converter for the requested type
     *
     * @param cupboard the cupboard instance
     * @param type     the type
     * @return a {@link EntityConverter} for the supplied type, or null if the type is not supported by this
     * factory.
     */
    public <T> EntityConverter<T> create(Cupboard cupboard, Class<T> type);
}
