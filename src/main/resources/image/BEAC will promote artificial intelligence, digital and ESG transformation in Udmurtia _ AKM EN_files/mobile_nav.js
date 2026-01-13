$(document).ready(function () {

    var documentWidth = $(document).outerWidth(), documentHeight = $(document).outerHeight();

    // $('#mobile-nav').mCustomScrollbar({
    //   theme: 'dark',
    //   autoHideScrollbar: true
    // });

    var createSlideout = function () {
      return new Slideout({
        'panel': document.getElementById('wrapper'),
        'menu': document.getElementById('mobile-nav'),
        'padding': $(document).outerWidth(),
        'tolerance': 70,
        'side': 'right',
        'touch': false
      });
    }

    var slideout = createSlideout();
    if (window.ResizeSensor) {
      window.ResizeSensor($('#mobile-nav').get(0), function() {
          slideout.destroy();
          slideout = createSlideout();
      });
    }

    $(document).on('click', '.js-mobile-nav-toggle', function () {
      slideout.toggle();
    });

    $(document).on('click', '.js-mobile-nav-open', function () {
      slideout.open();
    });

    $(document).on('click', '.js-mobile-nav-close', function () {
      slideout.close();
    });

    $('#mobile-nav').find('input').on('focus', function (e) {
        e.stopPropagation();
        console.log(123);
    });
});
