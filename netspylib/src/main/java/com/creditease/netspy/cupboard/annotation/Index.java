package com.creditease.netspy.cupboard.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation interface that allows one to create an index or composite index for a column/s
 * by specifying index_names, unique_index or unique_index_names in an Annotation. This is
 * only useful depending on the nature of your data and is only used for table creation / update.
 * A generated name will be used in table create or update unless
 * {@link com.creditease.netspy.cupboard.annotation.Index#indexNames()} or
 * {@link com.creditease.netspy.cupboard.annotation.Index#uniqueNames()} are used.
 * If the column is just annotated and no parameters are provided, a simple index with a generic name
 * will be created.
 *
 * Note that annotations are not processed by default. To enable processing of annotations construct an instance of Cupboard using {@link com.creditease.netspy.cupboard.CupboardBuilder} and call {@link com.creditease.netspy.cupboard.CupboardBuilder#useAnnotations()}
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Index {
    /**
     * @return names of indexes this field is going to be in. If another field shares the same indexName,
     * a composite index is created based on both fields under that indexName. It can be also used
     * for custom index naming.
     */
    CompositeIndex[] indexNames() default {};

    /**
     * @return whether this field should be an unique index or not. A generated name will be used in table create or update;
     * use {@link com.creditease.netspy.cupboard.annotation.Index#uniqueNames()} with one value if you want to use a custom name.
     */
    boolean unique() default false;

    /**
     * @return names of unique indexes this field is going to be in. If another field shares the same indexName,
     * a composite index is created based on both fields under that indexName. It can be also used
     * for custom unique index naming.
     */
    CompositeIndex[] uniqueNames() default {};

}