package com.erastedev.ciexplore.v1.domain.ports.out;

import com.erastedev.ciexplore.v1.domain.ports.in.ICommonEntity;

public abstract class AbstractCommonEntity<T> implements ICommonEntity<T> {

    @Override
    public String toString() {
        return String.format(
                "{\"class\":\"%s\", \"id\":\"%s\", \"name\":\"%s\", \"created\":\"%s\", \"deleted\":\"%s\", \"updated\":\"%s\", \"updateBy\":\"%s\"}",
                getClass(),
                getId(),
                getDisplayName(),
                getCreated(),
                getDeleted(),
                getUpdated(),
                getUpdateBy()
        );
    }
}
