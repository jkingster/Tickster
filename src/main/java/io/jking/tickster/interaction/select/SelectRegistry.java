package io.jking.tickster.interaction.select;

import io.jking.tickster.interaction.command.CommandRegistry;
import io.jking.tickster.interaction.core.Registry;
import io.jking.tickster.interaction.select.impl.CategoriesSelect;
import io.jking.tickster.interaction.select.impl.CommandSelect;

public class SelectRegistry extends Registry<AbstractSelect> {

    public SelectRegistry(CommandRegistry registry) {
        put("menu:help_categories", new CategoriesSelect(registry));
        put("menu:help_category", new CommandSelect(registry));
    }

}
