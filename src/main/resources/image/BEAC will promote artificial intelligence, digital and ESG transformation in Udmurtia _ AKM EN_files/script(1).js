if (!window.RS) {
  window.RS = {};
}

RS.MainMenu = (function() {
  var MainMenu = function(menuBlockId) {
    this.$el = $("#" + menuBlockId);
    var $itemsWithNesting = this.$el.find('.has-dropdown');

    this.$el.css({
      'overflow': 'hidden',
      'max-height': this.$el.height(),
      'opacity': '0',
    });

    $(window).on('resize', BX.debounce(BX.proxy(this.resizeMenu, this), 250));
    $(document).ready(BX.proxy(this.resizeMenu, this));

    $itemsWithNesting.on({
      mouseenter: this.reveal,
      mouseleave: this.conceal
    });
    $itemsWithNesting.one('mouseenter', $.proxy(this.hLoadItems, this));
  };

  MainMenu.prototype.resizeMenu = function() {
    var $items = this.$el.children(),
      $isMore = $items.filter('.is-more'),
      containerWidth = this.$el.outerWidth(),
      usedWidth = 0,
      lastIndex = 0,
      isMoreWidth = 0,
      $cloneItems;

    $items.show();
    isMoreWidth = $isMore.width();

    $items.filter(':not(.is-more)').each(function(index, item) {
      var itemWidth = $(item).width();

      if (usedWidth + itemWidth > containerWidth) {
        $items.filter(':gt(' + (index - 1) + ')').hide();
        return false;
      }

      usedWidth += itemWidth;

      return true;
    });

    while (true) {
      if (usedWidth + isMoreWidth > containerWidth) {
        var lastVisibleItem = $items.filter(':visible:last');
        usedWidth -= lastVisibleItem.width();
        lastVisibleItem.hide();
      } else {
        if ($items.length - 1 > $items.filter(':visible').length) {
          $cloneItems = $items.filter(':hidden:not(.is-more)').clone().show();
          $cloneItems.find('.b-main-menu-item__dropdown').remove();
          $isMore.find('.b-main-menu-item__dropdown').html($cloneItems);
          $isMore.show();
        } else {
          $isMore.hide();
          $isMore.find('.b-main-menu-item__dropdown').html('');
        }
        break;
      }
    }

    this.$el.css({
      'overflow': 'initial',
      'max-height': 'none',
      'opacity': '1',
      'visibility': 'visible'
    });
  };

  MainMenu.prototype.reveal = function() {
    var $item = $(this);
    var $target = $item.children('.b-main-menu-item__dropdown');

    if ($item.offset().left + 200 > $(window).width()) {
      $target.css('right', '0')
    }

    $target
      .velocity('stop')
      .velocity(
        'transition.slideDownIn', {
          duration: 250,
          delay: 0,
        }
      );
  };

  MainMenu.prototype.conceal = function() {
    var $target = $(this).children('.b-main-menu-item__dropdown');

    $target.velocity('stop').velocity(
      'transition.fadeOut', {
        duration: 100,
        delay: 0,
        complete: function() {
          $target.css('right', 'auto');
        }
      }
    );
  }

  MainMenu.prototype.hLoadItems = function(e) {
    var $el, $link, $nested, $spinner, sectionUrl;

    $el = $(e.currentTarget);
    $nested = $el.find('.l-mm-catalog-items');
    $link = $el.children('a.b-main-menu-item__link');
    $spinner = $('<div>').addClass('spinner');
    sectionUrl = $link.attr('href');

    $nested.html($spinner);

    if (sectionUrl) {
      $.getJSON(sectionUrl, {
        isAjax: 'Y',
        action: 'mm'
      }, function(result) {
        var $resultHTML, $resultItems;

        $resultHTML = $('<div>');

        if ((result.content || {}).items) {
          $resultHTML.append(result.content.items);
          $resultItems = $resultHTML.children();

          $nested.append($resultItems);

          if ((RS.Animations || {}).slideUpInElements) {
            RS.Animations.slideUpInElements($resultItems)
              .then(function() {
                $spinner.remove();
              });
          }
        }
      });
    }
  }

  return MainMenu;
})();
