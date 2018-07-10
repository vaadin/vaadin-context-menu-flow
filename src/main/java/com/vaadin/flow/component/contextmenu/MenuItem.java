/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component.contextmenu;

import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;

/**
 * Item component used inside {@link ContextMenu}
 * 
 * @author Vaadin Ltd.
 */
@SuppressWarnings("serial")
@Tag("vaadin-item")
@HtmlImport("frontend://bower_components/vaadin-item/src/vaadin-item.html")
public class MenuItem extends Component
        implements HasText, HasComponents, HasEnabled {

    private ContextMenu contextMenu;

    protected MenuItem(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    /**
     * Gets the menu component for which this item component belongs.
     * 
     * @return the {@link ContextMenu} that contains this item
     */
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    /**
     * Adds a click listener to this component.
     *
     * @param listener
     *            the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    public Registration addClickListener(
            ComponentEventListener<MenuItemClickEvent> listener) {
        return ComponentUtil.addListener(this, MenuItemClickEvent.class,
                listener);
    }

    /**
     * Event fired by clicking a {@link MenuItem}.
     * 
     * @author Vaadin Ltd.
     */
    @DomEvent("click")
    public static class MenuItemClickEvent extends ComponentEvent<MenuItem> {

        private Element targetChild;

        public MenuItemClickEvent(MenuItem source, boolean fromClient) {
            super(source, fromClient);
            targetChild = source.getContextMenu().getTargetChildElement();
        }

        public Optional<Component> getTargetChild() {
            if (getSource().getContextMenu()
                    .getTarget() instanceof PolymerTemplate) {
                throw new UnsupportedOperationException(
                        "Accessing the target child component is not supported "
                                + "when using a PolymerTemplate as the target "
                                + "of the ContextMenu.");
            }
            if (targetChild == null) {
                return Optional.empty();
            }
            return targetChild.getComponent();
        }

    }

}
