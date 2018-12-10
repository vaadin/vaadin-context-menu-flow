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

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.dom.Element;

/**
 * Item component used inside {@link ContextMenu}
 * 
 * @author Vaadin Ltd.
 */
@SuppressWarnings("serial")
@Tag("vaadin-item")
@HtmlImport("frontend://bower_components/vaadin-item/src/vaadin-item.html")
public class MenuItem extends Component
        implements HasText, HasComponents, ClickNotifier<MenuItem>, HasEnabled {

    private ContextMenuBase<?> contextMenu;

    private Element container;

    MenuItem(ContextMenuBase<?> contextMenu) {
        assert contextMenu != null;
        this.contextMenu = contextMenu;
    }

    /**
     * Gets the context-menu component that this item belongs to.
     * 
     * @return the context-menu component
     */
    public ContextMenuBase<?> getContextMenu() {
        return contextMenu;
    }

    public MenuItem addItem(String text,
            ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
        MenuItem menuItem = addItem(text);
        if (clickListener != null) {
            menuItem.addClickListener(clickListener);
        }
        return menuItem;
    }

    protected MenuItem addItem(String text) {
        MenuItem menuItem = new MenuItem(getContextMenu());
        menuItem.setText(text);
        getContainer().appendChild(menuItem.getElement());
        return menuItem;
    }

    private Element getContainer() {
        if (container == null) {
            container = new Element("div");
            getElement().appendVirtualChild(container);

            getElement().getNode()
                    .runWhenAttached(ui -> ui.beforeClientResponse(this,
                            context -> setContainerNodeId(ui)));
        }
        return container;
    }

    private void setContainerNodeId(UI ui) {
        int nodeId = container.getNode().getId();
        getElement().setProperty("_containerNodeId", nodeId);
    }

}
