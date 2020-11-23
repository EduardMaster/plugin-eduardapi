package net.eduard.api.lib.storage.api;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.annotations.StorageAttributes;

import java.lang.reflect.Field;

final public class StorageInfo implements Cloneable {
    private Class<?> type;
    private boolean reference;
    private boolean inline;
    private boolean indentifiable;
    private Field field;


    public Class<?> getMapKey() {
        return Extra.getTypeKey(field.getGenericType());
    }

    public Class<?> getMapValue() {
        return Extra.getTypeValue(field.getGenericType());
    }

    public Class<?> getListType() {
        if (field != null) {
            return Extra.getTypeKey(field.getGenericType());
        }
        return null;
    }

    public Class<?> getArrayType() {
        return getType().getComponentType();
    }

    public Storable<?> getStore() {
        return StorageAPI.getStore(getType());
    }

    public String getAlias() {
        return getAlias(getType());
    }


    public boolean isStorable() {
        return getStore() != null;
    }

    public StorageInfo clone() {
        try {
            return (StorageInfo) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void updateByType() {
        if (getType().isAnnotationPresent(StorageAttributes.class)) {
           StorageAttributes atr = getType().getAnnotation(StorageAttributes.class);
            update(atr);
        }
    }

    public void updateByStoreClass() {
        Storable<?> store = getStore(getType());
        if (store != null) {
            if (store.getClass().isAnnotationPresent(StorageAttributes.class)) {
                StorageAttributes atr = store.getClass().getAnnotation(StorageAttributes.class);
                update(atr);
            }
        }
    }

    public void update(StorageAttributes atr) {
        setReference(atr.reference());
        setInline(atr.inline());
        setIndentifiable(atr.indentificate());
    }

    public void updateByField() {
        if (getField() != null) {
            if (getField().isAnnotationPresent(StorageAttributes.class)) {
                StorageAttributes atr = getField().getAnnotation(StorageAttributes.class);
                update(atr);
            }
        }
    }

    public StorageInfo(Class<?> claz) {
        setType(claz);
    }

    public Class<?> getType() {
        return type;
    }

    public Storable<?> getStore(Class<?> claz) {
        return StorageAPI.getStore(claz);
    }

    public String getAlias(Class<?> claz) {
        return StorageAPI.getAlias(claz);
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public boolean isReference() {
        return reference;
    }

    public void setReference(boolean reference) {
        this.reference = reference;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public boolean isIndentifiable() {
        return indentifiable;
    }

    public void setIndentifiable(boolean indentifiable) {
        this.indentifiable = indentifiable;
    }
}