$(document).ready(function() {
  var $progressBar = $('#read-progress-bar'),
    $content = $('.js-news-detail'),
    contentHeight;

  if ($progressBar.length == 0) {
    $progressBar = $('<div class="progress-reading"></div>')
    $('body').append($progressBar);
  }

  contentHeight = $content.outerHeight();
  windowHeight = $(window).outerHeight();

  if (window.ResizeSensor) {
    window.ResizeSensor($content.get(0), function() {
      contentHeight = $content.outerHeight();
      windowHeight = $(window).outerHeight();
    });
  }

  $(window).scroll(function() {
    var percent     = 0,
			contentOffset  = $content.offset().top,
			windowOffset = $(window).scrollTop();

    if (windowOffset > contentOffset) {
      percent = 100 * (windowOffset - contentOffset) / (contentHeight - windowHeight);
    }

    $progressBar.css('width', percent + '%')
  });
});
