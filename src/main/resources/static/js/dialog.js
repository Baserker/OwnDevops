function Dialog(options) {
	this.tpl = {
        close: '<a class="ui-dialog-close" title="关闭"><span class="ui-icon ui-icon-delete"></span></a>',
        mask: '<div class="ui-mask"></div>',
        title: '<div class="ui-dialog-title">'+
                    '<h3><%=title%></h3>'+
                '</div>',
        wrap: '<div class="ui-dialog">'+
            '<div class="ui-dialog-content"></div>'+
            '<% if(btns){ %>'+
            '<div class="ui-dialog-btns">'+
            '<% for(var i=0, length=btns.length; i<length; i++){var item = btns[i]; %>'+
            '<a class="ui-btn ui-btn-<%=item.index%>" data-key="<%=item.key%>"><%=item.text%></a>'+
            '<% } %>'+
            '</div>'+
            '<% } %>' +
            '</div> '
    };
	this.root = function(m) {
		return this._el = m || this._el;
	};
	this.data = function(m, o) {
		var n = this._data;
		if (typeof(m) == "object") {
			return $.extend(n, m)
		} else {
			return o != undefined? n[m] = o: n[m];
		}
	};
	this.parseTpl = function(g, f) {
		var d = "var __p=[],print=function(){__p.push.apply(__p,arguments);};with(obj||{}){__p.push('" + g.replace(/\\/g, "\\\\").replace(/'/g, "\\'").replace(/<%=([\s\S]+?)%>/g,
		function(h, i) {
			return "'," + i.replace(/\\'/g, "'") + ",'"
		}).replace(/<%([\s\S]+?)%>/g,
		function(h, i) {
			return "');" + i.replace(/\\'/g, "'").replace(/[\r\n\t]/g, " ") + "__p.push('"
		}).replace(/\r/g, "\\r").replace(/\n/g, "\\n").replace(/\t/g, "\\t") + "');}return __p.join('');";
		var e = new Function("obj", d);
		return f ? e(f) : e
	};
	this._data = {
		autoOpen: true,
		buttons: null,
		closeBtn: true,
		mask: true,
		//width: 300,
		width: "auto",
		height: "auto",
		title: null,
		content: null,
		scrollMove: true,
		container: null,
		maskClick: null,
		position: null,
		footer: null  //提示框底部，在按钮下方
	};
	for ( var key in options ) {
		this._data[key] = options[key];
	}
	this._init();
	return this._wrap;
};

Dialog.prototype = {
	version: '',
	getWrap: function() {
		return this._data._wrap
	},
	_setup: function() {
		var d = this._data;
		d.content = d.content || this._el.show();
		d.title = d.title || this._el.attr("title")
	},
	_init: function() {
		var g = this,
		h = g._data,
		f, e = 0,
		d = $.proxy(g._eventHandler, g),
		j = {};
		h._container = $(h.container || "body"); (h._cIsBody = h._container.is("body")) || h._container.addClass("ui-dialog-container");
		j.btns = f = [];
		h.buttons && $.each(h.buttons,
		function(i) {
			if (i != "text") {
				f.push({
					text: g._data.buttons.text[e],
					key: i,
					index: ++e
				});
			}
		});
		
		h._mask = h.mask ? $(this.tpl.mask).appendTo(h._container) : null;
		
        j.footer = h.footer;

		h._wrap = $(this.parseTpl(this.tpl.wrap, j)).appendTo(h._container);
		h._content = $(".ui-dialog-content", h._wrap);
		h._title = $(this.tpl.title);
		h._close = h.closeBtn && $(this.tpl.close);   //.highlight("ui-dialog-close-hover");
		g._el = g._el || h._content;
		g.title(h.title);
		g.content(h.content);
		h._wrap.css({
			width: h.width,
			//"min-width": h.width,
			height: h.height
		});
		$(window).on("ortchange", d);
		h._wrap.on("click", d);
		h._mask && h._mask.on("click", d);
		h.autoOpen && g.root().one("init",
		function() {
			g.open();
		});
	},
	_eventHandler: function(j) {
		var h = this,
		d, g, i = h._data,
		f;
		switch (j.type) {
		case "ortchange":
			this.refresh();
			break;
		case "touchmove":
			i.scrollMove && j.preventDefault();
			break;
		case "click":
			if (i._mask && ($.contains(i._mask[0], j.target) || i._mask[0] === j.target)) {
				return h.trigger("maskClick")
			}
			g = i._wrap.get(0);
			if ((d = $(j.target).closest(".ui-dialog-close", g)) && d.length) {
				h.destroy()
			} else {
				if ((d = $(j.target).closest(".ui-dialog-btns .ui-btn", g)) && d.length) {
					f = i.buttons[d.attr("data-key")];
					f && f.apply(h, arguments)
				}
			}
		}
	},
	_calculate: function() {
		var i = this,
		j = i._data,
		h, k, e = document.body,
		g = {},
		d = j._cIsBody,
		f = Math.round;
		j.mask && (g.mask = d ? {
			width: "100%",
			height: Math.max(e.scrollHeight, e.clientHeight) - 1
		}: {
			width: "100%",
			height: "100%"
		});
		h = j._wrap.offset();
		k = $(window);
		
		//如果内容部分高度 大于屏幕高度的75%， 则改变高度，现实滚动条
		if( j._content.height() > $(window).height() * 0.55) {
		    var t = new Date().getTime();
		    j._content.html('<article id="article_'+t+'" class="clearfix" style="width:100%;height:100%;"><div>' + j._content.html() + '</div></article>')
		    j._content.height(Core.getWindowHeight() * 0.55);
            var iscroll1 = new IScroll('#article_'+t, {mouseWheel : true});
		}
		g.wrap = {
			left: "50%",
			marginLeft: -f(h.width / 2) + "px",
			//top: d ? f(k.height() / 2) + window.pageYOffset: "50%",
			// 手机输入框状态 如果用 计算的px 定位有问题
			top: "50%",
			marginTop: -f(j._wrap.height() / 2) + "px"
		};
		return g
	},
	refresh: function() {
		var e = this,
		g = e._data,
		d, f;
		if (g._isOpen) {
			f = function() {
				d = e._calculate();
				d.mask && g._mask.css(d.mask);
				g._wrap.css(d.wrap)
			};
			if ($.os.ios && document.activeElement && /input|textarea|select/i.test(document.activeElement.tagName)) {
				document.body.scrollLeft = 0;
				$.later(f, 200)
			} else {
				f()
			}
		}
		return e
	},
	open: function(d, f) {
		var e = this._data;
		e._isOpen = true;
		e._wrap.css("display", "block");
		e._mask && e._mask.css("display", "block");
		d !== undefined && this.position ? this.position(d, f) : this.refresh();
	},
	close: function() {
		var d, e = this._data;
		d = b.Event("beforeClose");
		this.trigger(d);
		if (d.defaultPrevented) {
			return this
		}
		e._isOpen = false;
		e._wrap.css("display", "none");
		e._mask && e._mask.css("display", "none");
		b(document).off("touchmove", this._eventHandler);
		return this.trigger("close")
	},
	title: function(e) {
		var d = this._data,
		f = e !== undefined;
		if (f) {
			e = (d.title = e) ? "<h3>" + e + "</h3>": e;
			d._title.html(e)[e ? "prependTo": "remove"](d._wrap);
			d._close && d._close.prependTo(d.title ? d._title: d._wrap)
		}
		return f ? this: d.title
	},
	content: function(e) {
		var d = this._data,
		f = e !== undefined;
		f && d._content.empty().append(d.content = e);
		return f ? this: d.content
	},
	destroy: function() {
		var e = this._data,
		d = this._eventHandler;
		$(window).off("ortchange", d);
		$(document).off("touchmove", d);
		e._wrap.off("click", d).remove();
		e._mask && e._mask.off("click", d).remove();
		e._close && e._close.highlight && e._close.highlight();
	},
	trigger: function(n, o) {
        n = typeof n == 'string' ? $.Event(n) : n;
        var p = this.data(n.type),
        m;
        if (p && $.isFunction(p)) {
            n.data = o;
            m = p.apply(this, [n].concat(o));
            if (m === false || n.defaultPrevented) {
                return this
            }
        }
        this.root().trigger(n, o);
        return this
    }
}