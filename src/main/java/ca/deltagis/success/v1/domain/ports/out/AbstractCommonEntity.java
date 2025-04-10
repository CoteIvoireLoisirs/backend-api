package ca.deltagis.success.v1.domain.ports.out;

import ca.deltagis.success.v1.domain.ports.in.ICommonEntity;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-25 13:43:11
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/domain/ports/out/AbstractCommonEntity.java
 * @Description: This is the default configuration, you can modify it in Settings > Tools > File Description.
 */
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
