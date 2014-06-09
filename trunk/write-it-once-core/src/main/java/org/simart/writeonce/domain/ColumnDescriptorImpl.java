package org.simart.writeonce.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Column;

import org.simart.writeonce.common.BeanMethodDescriptor;
import org.simart.writeonce.common.ColumnDescriptor;
import org.simart.writeonce.common.ColumnNameResolver;

public class ColumnDescriptorImpl implements ColumnDescriptor {

    private final Context context;
    private final Field field;
    private final Method method;
    private final Column column;

    public ColumnDescriptorImpl(Context context, Field field) {
        super();
        this.context = context;
        this.field = field;
        this.method = null;
        this.column = this.field.getAnnotation(Column.class);
    }

    public ColumnDescriptorImpl(Context context, Method method) {
        super();
        this.context = context;
        this.field = null;
        this.method = method;
        this.column = this.method.getAnnotation(Column.class);
    }

    @Override
    public String getName() {
        if (this.column != null && !this.column.name().isEmpty()) {
            return this.column.name();
        }
        final ColumnNameResolver columnNameResolver = context.getColumnNameResolver();
        if (columnNameResolver == null) {
            throw new RuntimeException("undefined column name resolver");
        }
        if (this.field != null) {
            return columnNameResolver.getName(field.getName());
        } else {
            return columnNameResolver.getName(context.create(BeanMethodDescriptor.class, this.method).getField().getName());
        }
    }

    @Override
    public String getType() {
        if (this.field != null) {
            return context.getColumnTypeResolver().getType(column, this.field.getType());
        } else {
            return context.getColumnTypeResolver().getType(column, this.method.getReturnType());
        }
    }

    @Override
    public String getFullType() {
        if (this.field != null) {
            return context.getColumnTypeResolver().getFullType(column, this.field.getType());
        } else {
            return context.getColumnTypeResolver().getFullType(column, this.method.getReturnType());
        }
    }

    @Override
    public Integer getLength() {
        return this.column == null ? 0 : this.column.length();
    }

    @Override
    public Integer getPrecision() {
        return this.column == null ? 0 : this.column.precision();
    }

    @Override
    public Integer getScale() {
        return this.column == null ? 0 : this.column.scale();
    }

    @Override
    public Boolean isNullable() {
        return this.column == null ? false : this.column.nullable();
    }

}
