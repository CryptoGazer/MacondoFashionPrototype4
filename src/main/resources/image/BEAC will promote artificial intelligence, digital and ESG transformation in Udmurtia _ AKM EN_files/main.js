(function () {
	window.RS = window.RS || {};

	var $doc = $(document);

	RS.Options = {
		ajaxLoad: {
			defaultParams: {
				data: {
					isAjax: 'Y'
				}
			}
		},

		lazyLoadImagesSelctor: '.is-lazy-img',
		lazyLoadSectionSelector: '.js-lazyload-section',
	}

	RS.Utils = {
		callFunctionByName: function (functionName, context, callContext) {
			var args = Array.prototype.slice.call(arguments, 3);
			var namespaces = functionName.split(".");
			var func = namespaces.pop();

			context = context || window;

			for (var i = 0; i < namespaces.length; i++) {
				context = context[namespaces[i]];
			}

			return context[func].apply(callContext, args);
		},

		callFunctions: function (functions, context, callContext) {
			var args = Array.prototype.slice.call(arguments, 3);

			functions.forEach(function (fn) {
				RS.Utils.callFunctionByName.apply({}, BX.util.array_merge([fn, context, callContext], args));
			})
		},

		lazyLoadImages: function ($elements) {
			$elements.Lazy({
				effect: 'fadeIn',
				effectTime: 500,
				afterLoad: function (el) {
					if (el.prop('tagName').toLowerCase() == 'picture') {
						el.addClass('loaded');
						el.children('[data-empty]').remove();
					}
				}
			});
		},

		lazyLoadSection: function ($sections) {
			$sections.each(function () {
				var $section = $(this);

				if (!$section.hasClass('is-wait-loading')) {
					return;
				}

				$section.find('.is-lazy').lazy({
					bind: "event",
					delay: 0,
					onFinishedAll: function () {
						var $preload = $section.find('.js-lazyload-section__preload');
						var $container = $section.find('.js-lazyload-section__container');

						$container
							.css({
								'opacity': 0,
								'display': 'block'
							})
							.velocity('stop')
							.velocity('transition.fadeIn', {
								duration: 1000,
								begin: function () {
									// Fix Owl Carousel width
									if ($.fn.owlCarousel) {
										$container.find('[data-slider]')
											.owlCarousel('invalidate', 'width')
											.owlCarousel('refresh');
									}
								}
							});

						$preload
							.velocity('stop')
							.velocity(
								'transition.fadeOut', {
									duration: 1500,
									delay: 0,
									complete: function () {
										$section.removeClass('is-wait-loading');
									}
								});

						if (window.jarallax && $section.find('.js-lazyload-section__parallax').length > 0) {
							jarallax($section.find('.js-lazyload-section__parallax').get(0), {
								speed: 0.2
							});
						}
					}
				});
			});
		}

	};

	RS.Animations = {
		slideUpElements: function ($elements) {
			var d = $.Deferred();
			var display = $elements.css('display');

			$elements.hide().velocity('stop').velocity('transition.slideUpIn', {
				stagger: 100,
				duration: 500,
				display: display,
				complete: function () {
					d.resolve();
				}
			});

			return d.promise();
		},

		slideUpInElements: function ($elements) {
			var d = $.Deferred();

			$elements
				.hide()
				.css('opacity', 0)
				.velocity('stop').velocity('transition.slideUpIn', {
					stagger: 100,
					duration: 500,
					complete: function () {
						d.resolve();
					}
				});

			return d.promise();
		}

	};

	RS.Handlers = {

		// Section Loading Handlers
		appendSectionItems: function (params, result) {
			var result = BX.parseJSON(result);

			if (params.itemsBlockId && ((result || {}).content || {}).items) {
				$("#" + params.itemsBlockId).append(result.content.items);
			}
		},

		replaceSectionItems: function (params, result) {
			var result = BX.parseJSON(result);

			if (params.itemsBlockId && ((result || {}).content || {}).items) {
				$("#" + params.itemsBlockId).html(result.content.items);
			}
		},

		showSectionItems: function (params, result) {
			var $items = $("#" + params.itemsBlockId).find('> .row:last-child');

			if ($items.length > 0) {
				RS.Animations.slideUpElements($items.find(params.itemSelector || '.js-item'));
				RS.Utils.lazyLoadImages($items.find(RS.Options.lazyLoadImagesSelctor));
			}
		},

		updateSectionNavigation: function (params, result) {
			var result = BX.parseJSON(result);

			if (params.loadMoreBlockId && ((result || {}).content || {}).loadMore) {
				$("#" + params.loadMoreBlockId).replaceWith(result.content.loadMore);
			}

			if (params.ajaxNavBlockId && ((result || {}).content || {}).ajaxNavigation) {
				$("#" + params.ajaxNavBlockId).html(result.content.ajaxNavigation);
			}

			if (window.history && result.navParams) {
				navUriParams = {};
				navUriParams[result.navParams.navParamNomer] = result.navParams.currentPage

				window.history.pushState({},
					"",
					BX.util.add_url_param(window.location.href, navUriParams)
				);
			}
		},

		startSectionLoading: function (params) {
			if (this.id == params.loadMoreBlockId) {
				$(this).addClass('is-loading');
			} else {
				$("#" + params.itemsBlockId).css({
					'min-height': $("#" + params.itemsBlockId).height() + 'px'
				});
				$("#" + params.itemsBlockId).addClass('is-loading');
			}
		},

		stopSectionLoading: function (params) {
			if (this.id == params.loadMoreBlockId) {
				$(this).removeClass('is-loading');
			} else {
				$("#" + params.itemsBlockId).removeClass('is-loading');

				setTimeout(function () {
					//debugger;
					$("#" + params.itemsBlockId).removeAttr('style');
				}, 250);
			}
		}

	}

	$doc.ready(function () {
		var $wrapper = $("#wrapper");

		// Custom srollbar
		$('[data-use-custom-scrollbar]').mCustomScrollbar({
			autoHideScrollbar: true
		});

		/* side aside */
		var openSideAside = function () {
			var $sideAside = $('.js-side-aside');

			$sideAside.addClass('is-open');
			$wrapper.addClass('has-overlay');

			$wrapper.one('click', function () {
				closeSideAside();
			});
		}

		var closeSideAside = function () {
			var $sideAside = $('.js-side-aside');
			$sideAside.removeClass('is-open');
			$wrapper.removeClass('has-overlay');
		}

		// Toggle
		$doc.on('click', '.js-sa-toggle', function (e) {
			e.preventDefault();

			var $sideAside = $('.js-side-aside');
			if (!$sideAside.hasClass('is-open')) {
				openSideAside();
			} else {
				closeSideAside();
			}
		});

		// open
		$doc.on('click', '.js-sa-open', function (e) {
			e.preventDefault();
			openSideAside();
		});

		// close
		$doc.on('click', '.js-sa-close', function (e) {
			e.preventDefault();
			closeSideAside();
		});
		/* /side aside */

		/* JS Sticky header */
		var $stickyHeader = $("#sticky-header");
		var offsetTop = $stickyHeader.offset().top;
		var checkHeaderSticky = function () {
			if ($doc.scrollTop() > offsetTop) {
				$stickyHeader.addClass('is-sticky');
			} else {
				$stickyHeader.removeClass('is-sticky');
			}
		}

		$stickyHeader.css({
			height: $stickyHeader.children().outerHeight(),
		});

		$(window).resize(BX.debounce(function () {
			$stickyHeader.css({
				height: $stickyHeader.children().outerHeight()
			});
		}, 250));

		$stickyHeader
			.addClass('is-sticky-ready')

		checkHeaderSticky();
		$doc.scroll(checkHeaderSticky);
		/* /JS Sticky header */

		/* Sticky content */
		$('.sticky-content').each(function () {
			var $elements = $(this).children('.l-page__sidebar, .col-lg-4');
			$elements.each(function () {
				var $wrap, wrapHeight = 0;

				$(this).wrapInner('<div style="padding-bottom: 1px; position: sticky; top: 0px;"></div>');

				$wrap = $(this).children().eq(0);

				Stickyfill.add($wrap.get(0));

				wrapHeight = $wrap.height();
				if (wrapHeight > $(window).outerHeight()) {
					$wrap.css('top', $(window).outerHeight() - wrapHeight);
				}

				if (window.ResizeSensor) {
					window.ResizeSensor(this, function () {
						wrapHeight = $wrap.height();
						if (wrapHeight > $(window).outerHeight()) {
							$wrap.css('top', $(window).outerHeight() - wrapHeight);
						}
					});
				}

			});
		});
		/* /Sticky content */

		/* Sliders */
		if (RS.Slider) {
			window.RS.sliders = [];
			$('[data-slider=true]').each(function (key, item) {
				var name = $(item).data('slider-name') || 'slider_' + Object.keys(window.RS.sliders).length + 1;
				var slider = new RS.Slider(name, this);
				slider.init();

				window.RS.sliders[name] = slider;
			});
		}
		/* /Sliders */

		/* Ajax Load */
		var isAjaxDuring = false;
		var ajaxLoadCacheData = {};
		var ajaxLoadFn = function (url, params) {
			var ajaxParams, serializeData, cacheKey;

			serializeData = JSON.stringify(params.data || {});
			cacheKey = serializeData + url;

			if (params.useCache && ajaxLoadCacheData[cacheKey]) {
				return $.Deferred(function (d) {
					d.resolve(ajaxLoadCacheData[cacheKey]);
				}).promise();
			}

			ajaxParams = {};
			ajaxParams.url = url;

			if (params.method) {
				ajaxParams.method = params.method;
			}

			ajaxParams.data = params.data;

			return $.ajax(ajaxParams)
				.then(function (result) {
					if (params.useCache) {
						ajaxLoadCacheData[cacheKey] = result;
					}

					return result;
				});
		}

		var ajaxLoad = function (obj) {
			var params, url, callbacks;

			url = obj.getAttribute('href');
			params = (
				BX.type.isPlainObject(obj.getAttribute('data-ajax-load')) ?
				obj.getAttribute('data-ajax-load') : BX.parseJSON(obj.getAttribute('data-ajax-load'))
			);

			BX.merge(params, RS.Options.ajaxLoad.defaultParams);
			callbacks = BX.merge(params.callbacks, {
				before: [],
				success: [],
				after: []
			});

			RS.Utils.callFunctions(callbacks.before, window, obj, params);

			return ajaxLoadFn(url, params)
				.done(function (result) {
					RS.Utils.callFunctions(callbacks.success, window, obj, params, result);
					return result;
				})
				.always(function (result) {
					RS.Utils.callFunctions(callbacks.after, window, obj, params);
					setTimeout(function () {
						isAjaxDuring = false;
					}, 300);

					return result;
				});
		}

		$doc.on('click', '[data-ajax-load]', function (e) {
			e.preventDefault();

			if (!isAjaxDuring) {
				isAjaxDuring = true;
				ajaxLoad(this)
					.done(function () {
						isAjaxDuring = true;
					});
			}
		});

		var infiniteScrollChecker = function () {
			// Infinite scroll
			$('[data-infinite-scroll]:not(.js-infinite-scroll)')
				.addClass('js-infinite-scroll')
				.viewportChecker({
					repeat: true,
					offset: 60,
					callbackFunction: function (elem, action) {
						if (!isAjaxDuring && action == 'add') {
							isAjaxDuring = true;

							ajaxLoad(elem.get(0))
								.done(function () {

									setTimeout(function () {
										isAjaxDuring = false;
										infiniteScrollChecker();
									}, 1000);
								});
						}
					}
				});
		}
		infiniteScrollChecker();

		/* /Ajax Load */

		// Lazy Load Images
		RS.Utils.lazyLoadImages($(RS.Options.lazyLoadImagesSelctor));

		// Lazy Load Sections
		RS.Utils.lazyLoadSection($(RS.Options.lazyLoadSectionSelector));
	});
})();