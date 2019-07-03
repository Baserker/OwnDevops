/**
 * 与客户端之间的交互API
 * @author genx
 * @date 2019-05-23
 */
(function (window) {
    /**
     * 从url中提取参数
     * @param param
     * @returns {any}
     */
    var getParam = function (param) {
        var href = location.href;
        var g = new RegExp("(\\?|#|&)" + param + "=([^&#]*)(&|#|$)");
        var d = href.match(g);
        return d ? d[2] || "" : "";
    };

    /**
     * 设置key value 到 localStorage
     * @param key
     * @param value
     */
    var setLocalData = function (key, value) {
        window.localStorage && window.localStorage.setItem(key, JSON.stringify(value));
    };
    /**
     * 从 localStorage读取
     * @param key
     * @returns {Storage | any}
     */
    var getLocalData = function (key) {
        return window.localStorage && JSON.parse(window.localStorage.getItem(key));
    };

    /**
     * 构造一个回调函数
     * @param callBack
     * @param cyclic 是否循环使用的
     * @returns {string|null}
     */
    var buildCallBack = function (callBack, cyclic) {
        var time = new Date().getTime();
        window['callback' + time] = function () {
            if (callBack) {
                callBack.apply(this, arguments);
            } else {
                window.location.reload();
            }
            if(!(cyclic)) {
                delete window['callback' + time];
            }
        };
        return 'callback' + time;
    };

    window.loginCallBack = function(){

    };

    var loginIntervalId;

    window.caimaoApi = {
        /**
         * 当前h5是否是在手机客户端webview中加载的
         * @returns {boolean}
         */
        isInApp: function () {
            return !!(window.caimaoApp);
        },
        /**
         * 获取userToken
         * @returns {Storage|any}
         */
        getUserToken: function () {
            if (window.caimaoApp && window.caimaoApp.getUserToken) {
                var userToken = window.caimaoApp.getUserToken();
                //针对IOS的兼容
                setLocalData("userToken", userToken);
                return userToken
            } else {
                return getParam("userToken") || getLocalData("userToken");
            }
        },
        /**
         * 去登录
         * @param callBack 回调函数
         */
        goToLogin: function (callBack) {
            window.loginCallBack = function(){
                delete window.loginCallBack;
                callBack && callBack();

            };
            if (window.caimaoApp && window.caimaoApp.goToLogin) {
                caimaoApp.goToLogin('loginCallBack');
            } else {
                location.href = 'caimao:action=login';
            }

            if (window.caimaoApp && window.caimaoApp.getUserToken) {
                //以下是对老版本 客户端没有回调的 兼容
                if (loginIntervalId) {
                    //关闭前面的定时任务
                    clearInterval(loginIntervalId);
                }
                //开启一个定时任务
                loginIntervalId = setInterval(function () {
                    if (window.caimaoApp.getUserToken()) {
                        //清除定时器
                        clearInterval(loginIntervalId);
                        //大兄弟 终于拿到 userToken了
                        window.loginCallBack && window.loginCallBack();
                    }
                }, 1000);
            }
        },
        /**
         * 去充值
         * TODO 不知道有没有回调函数 需要跟客户端沟通一下
         */
        goToRecharge: function () {
            if (window.caimaoApp && window.caimaoApp.goToRecharge) {
                caimaoApp.goToRecharge();
            } else {
                location.href = 'caimao:action=recharge';
            }
        },
        /**
         * 设置分享(标准格式)
         * @param title
         * @param desc
         * @param link
         * @param imgUrl
         * @param callback
         * @returns {boolean}
         */
        setShareData: function (title, desc, link, imgUrl, callback) {
            if (window.caimaoApp && window.caimaoApp.setShareData) {
                caimaoApp.setShareData(title, desc, link, imgUrl, buildCallBack(callback, true));
                return true;
            } else {
                return false;
            }
        },
        /**
         * 设置分享(只是一张图片)
         * @param imgUrl 图片的网络地址
         * @param callback 分享成功(无法判断真的分享成功)后调用
         * @returns {boolean}
         */
        setShareImg: function (imgUrl, callback) {
            if (window.caimaoApp && window.caimaoApp.setShareImg) {
                caimaoApp.setShareImg(imgUrl, buildCallBack(callback, true));
                return true;
            } else {
                return false;
            }
        },
        /**
         * 在h5网页端调起分享弹框(必须先初始化分享 setShareData 或 setShareImg)
         * @returns {boolean}
         */
        shareBtnClick: function () {
            if (window.caimaoApp && window.caimaoApp.shareBtnClick) {
                caimaoApp.shareBtnClick();
                return true;
            } else {
                return false;
            }
        },
        /**
         * 跳转到购彩界面
         * @param lotteryId
         */
        goToLotteryBuy: function (lotteryId) {
            if (window.caimaoApp && window.caimaoApp.goToLotteryBuy) {
                caimaoApp.goToLotteryBuy(lotteryId);
            } else {
                location.href = 'caimao:action=lotteryBuy' + (lotteryId ? '?lotteryId=' + lotteryId : '');
            }
        },
        /**
         * 跳转到手机绑定界面
         */
        goToPhoneBind: function(){
            if (window.caimaoApp && window.caimaoApp.goToPhoneBind) {
                caimaoApp.goToPhoneBind();
            } else {
                location.href = 'caimao:action=phoneBind';
            }
        },
        /**
         * 跳转到红包券列表
         */
        goToPresentTicket: function(){
            if (window.caimaoApp && window.caimaoApp.goToPresentTicket) {
                caimaoApp.goToPresentTicket();
            } else {
                location.href = 'caimao:action=presentTicket';
            }
        },
        /**
         * 跳转到注册界面
         */
        goToRegist: function(){
            if (window.caimaoApp && window.caimaoApp.goToRegist) {
                caimaoApp.goToRegist();
            } else {
                location.href = 'caimao:action=regist';
            }
        },
        /**
         * 跳转到用户个人中心
         */
        goToUserIndex: function(){
            if (window.caimaoApp && window.caimaoApp.goToUserIndex) {
                caimaoApp.goToUserIndex();
            } else {
                location.href = 'caimao:action=myaccount';
            }
        },
        /**
         * 跳转到用户资料界面
         */
        goToMyDatum: function(){
            if (window.caimaoApp && window.caimaoApp.goToMyDatum) {
                caimaoApp.goToMyDatum();
            } else {
                location.href = 'caimao:action=mydatum';
            }
        },
        /**
         * 跳转到身份信息绑定界面
         */
        goToIdentityBind: function(){
            if (window.caimaoApp && window.caimaoApp.goToIdentityBind) {
                caimaoApp.goToIdentityBind();
            } else {
                location.href = 'caimao:action=identityBind';
            }
        },

        //以下方法 客户端还为支持

        /**
         * 获取产品名称
         * @returns {null|*|null}
         */
        getAppName: function(){
            if (window.caimaoApp && window.caimaoApp.getAppName) {
                return caimaoApp.getAppName();
            } else {
                return null;
            }
        },
        /**
         * 获取分支名称
         * @returns {null|*|null}
         */
        getBranchName: function(){
            if (window.caimaoApp && window.caimaoApp.getBranchName) {
                return caimaoApp.getBranchName();
            } else {
                return null;
            }
        },
        /**
         * 获取渠道包名称
         * @returns {null|*|null}
         */
        getSourceUserName: function(){
            if (window.caimaoApp && window.caimaoApp.getSourceUserName) {
                return caimaoApp.getSourceUserName();
            } else {
                return null;
            }
        },
        /**
         * 获取版本号
         * @returns {null|*|null}
         */
        getVersion: function(){
            if (window.caimaoApp && window.caimaoApp.getVersion) {
                return caimaoApp.getVersion();
            } else {
                return null;
            }
        },
        /**
         * 获取机型
         * @returns {null|*|null}
         */
        getEquipment: function(){
            if (window.caimaoApp && window.caimaoApp.getEquipment) {
                return caimaoApp.getEquipment();
            } else {
                return null;
            }
        },
        /**
         * 获取IMEI  IOS无法获取IMEI  可以返回 udid?
         * @returns {null|*|null}
         */
        getImei: function(){
            if (window.caimaoApp && window.caimaoApp.getImei) {
                return caimaoApp.getImei();
            } else {
                return null;
            }
        }
    };
})(window);