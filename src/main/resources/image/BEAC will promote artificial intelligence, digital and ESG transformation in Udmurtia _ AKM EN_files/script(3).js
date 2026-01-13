$(document).ready(function() {
  var $popup = $('#popup-search'),
    $wrapper = $('#wrapper'),
    $html = $('html');

  var popupSearchConceal = function() {
    $wrapper.removeClass('is-blur');
    $('.title-search-result').hide();

    $popup
      .velocity('stop')
      .velocity(
        'transition.fadeOut', {
          duration: 250,
          delay: 0,
          complete: function() {
            $popup
              .removeClass('is-open')
              .css('opacity', '1')
              .hide();

            $html.removeClass('no-scroll');
          }
        }
      );
  };

  var popupSearchReveal = function() {
    $popup.show();
    $popup.removeClass('is-open');
    $wrapper.removeClass('is-blur');

    if ($html.outerHeight() > $(window).outerHeight()) {
      $html.addClass('no-scroll');
    }

    $popup.on('click.close-popup', function(event) {
      if ($(event.target).is($popup)) {
        popupSearchConceal();
        $popup.off('click.close-popup');
      }
    });

    $(document).on('keyup.close-popup', function (event) {
      if (event.keyCode == 27) {
        popupSearchConceal();
        $popup.off('keyup.close-popup');
      }
    });

    setTimeout(function() {
      $popup.addClass('is-open');
      $wrapper.addClass('is-blur');
    });
  };

  $(document).on('click', '.js-search-reveal', function(event) {
    event.preventDefault();

    popupSearchReveal();
  });

  $(document).on('click', '.js-search-conceal', function(event) {
    event.preventDefault();

    popupSearchConceal();
  });
});
