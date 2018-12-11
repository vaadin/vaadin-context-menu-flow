window.Vaadin.Flow.contextMenuConnector = {

  // NOTE: This is for the TARGET component, not for the <vaadin-context-menu> itself
  init: function(target) {
    if (target.$contextMenuConnector) {
      return;
    }

    target.$contextMenuConnector = {

      openOnHandler: function(e) {
        e.preventDefault();
        this.$contextMenuConnector.openEvent = e;
        target.dispatchEvent(new CustomEvent('vaadin-context-menu-before-open'));
      },

      updateOpenOn: function(eventType) {
        this.removeListener();
        this.openOnEventType = eventType;

        if (Polymer.Gestures.gestures[eventType]) {
          Polymer.Gestures.addListener(target, eventType, this.openOnHandler);
        } else {
          target.addEventListener(eventType, this.openOnHandler);
        }
      },

      removeListener: function() {
        if (this.openOnEventType) {
          if (Polymer.Gestures.gestures[this.openOnEventType]) {
            Polymer.Gestures.removeListener(target, this.openOnEventType, this.openOnHandler);
          } else {
            target.removeEventListener(this.openOnEventType, this.openOnHandler);
          }
        }
      },

      openMenu: function(contextMenu) {
        contextMenu.open(this.openEvent);
      },

      removeConnector: function() {
        this.removeListener();
        target.$contextMenuConnector = undefined;
      }

    };
  },

  initMenuConnector: function(menu, appId, nodeId) {
    // if (menu.$connector) {
    //   return;
    // }

    menu._containerNodeId = nodeId;

    menu.$connector = {

      appId: appId,

      _getContainer: function(nodeId) {
        try {
          return window.Vaadin.Flow.clients[this.appId].getByNodeId(nodeId);
        } catch (error) {
          console.error("Could not get node %s from app %s", this.nodeId, this.appId);
          console.error(error);
        }
      },

      _getChildItems: function(parent) {
        const container = this._getContainer(parent._containerNodeId);
        const items = Array.from(container.children).map(child => {
          // const item = {label: child.textContent};
          const item = {component: child};
          if (child instanceof Vaadin.ItemElement && child._containerNodeId) {
            item.children = this._getChildItems(child);
          }
          return item;
        });
        console.log(items);
        return items;
      },

      _updateChildren: function() {
        const items = menu.$connector._getChildItems(menu);
        console.log('all items', items);
        menu.items = items;
      }

    }
  }
}
