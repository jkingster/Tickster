package io.jking.tickster.interaction;


public interface InteractionImpl<T extends AbstractContext<?>> {

    void onInteraction(T context);

    String componentId();

}
