/*
 * Copyright 2000-2018 Vaadin Ltd.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.dom.Element;

public class SubMenu {

    private MenuItem parentMenuItem;
    private List<Component> children = new ArrayList<>();

    SubMenu(MenuItem parentMenuItem) {
        this.parentMenuItem = parentMenuItem;
    }

    /**
     * Creates and adds a new item component to this context menu with the given
     * text content.
     *
     * @param text
     *            the text content for the created menu item
     * @return the created menu item
     */
    protected MenuItem addItem(String text) {
        MenuItem menuItem = new MenuItem(parentMenuItem.getContextMenu());
        menuItem.setText(text);
        add(menuItem);
        return menuItem;
    }

    /**
     * Creates and adds a new item component to this context menu with the given
     * component inside.
     *
     * @param component
     *            the component to add to the created menu item
     * @return the created menu item
     */
    protected MenuItem addItem(Component component) {
        MenuItem menuItem = new MenuItem(parentMenuItem.getContextMenu());
        add(menuItem);
        menuItem.add(component);
        return menuItem;
    }

    /**
     * Adds a new item component with the given text content and click listener
     * to the context menu overlay.
     * <p>
     * This is a convenience method for the use case where you have a list of
     * highlightable {@link MenuItem}s inside the overlay. If you want to
     * configure the contents of the overlay without wrapping them inside
     * {@link MenuItem}s, or if you just want to add some non-highlightable
     * components between the items, use the {@link #add(Component...)} method.
     *
     * @param text
     *            the text content for the new item
     * @param clickListener
     *            the handler for clicking the new item, can be {@code null} to
     *            not add listener
     * @return the added {@link MenuItem} component
     * @see #addItem(Component, ComponentEventListener)
     * @see #add(Component...)
     */
    public MenuItem addItem(String text,
            ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
        MenuItem menuItem = addItem(text);
        if (clickListener != null) {
            menuItem.addClickListener(clickListener);
        }
        return menuItem;
    }

    /**
     * Adds a new item component with the given component and click listener to
     * the context menu overlay.
     * <p>
     * This is a convenience method for the use case where you have a list of
     * highlightable {@link MenuItem}s inside the overlay. If you want to
     * configure the contents of the overlay without wrapping them inside
     * {@link MenuItem}s, or if you just want to add some non-highlightable
     * components between the items, use the {@link #add(Component...)} method.
     *
     * @param component
     *            the component inside the new item
     * @param clickListener
     *            the handler for clicking the new item, can be {@code null} to
     *            not add listener
     * @return the added {@link MenuItem} component
     * @see #addItem(String, ComponentEventListener)
     * @see #add(Component...)
     */
    public MenuItem addItem(Component component,
            ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
        MenuItem menuItem = addItem(component);
        if (clickListener != null) {
            menuItem.addClickListener(clickListener);
        }
        return menuItem;
    }

    public void add(Component... components) {
        Objects.requireNonNull(components, "Components to add cannot be null");
        for (Component component : components) {
            Objects.requireNonNull(component,
                    "Component to add cannot be null");
            children.add(component);
            updateChildren();
        }
    }

    public void remove(Component... components) {
        Objects.requireNonNull(components,
                "Components to remove cannot be null");
        for (Component component : components) {
            Objects.requireNonNull(component,
                    "Component to remove cannot be null");
            if (children.contains(component)) {
                children.remove(component);
                updateChildren();
            } else {
                throw new IllegalArgumentException("The given component ("
                        + component + ") is not a child of this component");
            }
        }
    }

    public void removeAll() {
        children.clear();
        updateChildren();
    }

    public void addComponentAtIndex(int index, Component component) {
        Objects.requireNonNull(component, "Component should not be null");
        if (index < 0) {
            throw new IllegalArgumentException(
                    "Cannot add a component with a negative index");
        }
        children.add(index, component);
        updateChildren();
    }

    public Stream<Component> getChildren() {
        return children.stream();
    }

    /**
     * Gets the items added to this component (the children of this component
     * that are instances of {@link MenuItem}).
     *
     * @return the {@link MenuItem} components in this context menu
     * @see #addItem(String, ComponentEventListener)
     */
    public List<MenuItem> getItems() {
        return getChildren().filter(MenuItem.class::isInstance)
                .map(child -> (MenuItem) child).collect(Collectors.toList());
    }

    private void updateChildren() {
        parentMenuItem.getContextMenu().updateChildren();
    }

    private Element getElement() {
        return parentMenuItem.getElement();
    }

    public MenuItem getParentMenuItem() {
        return parentMenuItem;
    }
}
