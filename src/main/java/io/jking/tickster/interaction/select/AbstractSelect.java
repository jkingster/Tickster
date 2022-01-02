package io.jking.tickster.interaction.select;

import io.jking.tickster.interaction.core.impl.SelectSender;

public abstract class AbstractSelect {
    private final String selectId;

    public AbstractSelect(String selectId) {
        this.selectId = selectId;
    }

    public abstract void onSelectPress(SelectSender sender);

    public String getSelectId() {
        return selectId;
    }

}
