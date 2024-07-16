/**
 * 时间戳
 * @param {*} timestamp  时间戳
 */
 const timestampToTime = (timestamp) => {
    if(timestamp==null||timestamp==''){
        return ''
    }
    let date = new Date(timestamp) //时间戳为10位需*1000，时间戳为13位的话不需乘1000
    let Y = date.getFullYear() + '-'
    let M =
        (date.getMonth() + 1 < 10 ?
            '0' + (date.getMonth() + 1) :
            date.getMonth() + 1) + '-'
    let D =
        (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' '
    let h =
        (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':'
    let m =
        (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) +
        ':'
    let s =
        date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
    return Y + M + D + h + m + s
};

const timestampToDate = (timestamp) => {
    let date = new Date(timestamp) //时间戳为10位需*1000，时间戳为13位的话不需乘1000
    let Y = date.getFullYear() + '-'
    let M =
        (date.getMonth() + 1 < 10 ?
            '0' + (date.getMonth() + 1) :
            date.getMonth() + 1) + '-'
    let D =
        (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' '
    return Y + M + D
};

const timestampToHM = (timestamp) => {
    let date = new Date(timestamp) //时间戳为10位需*1000，时间戳为13位的话不需乘1000
    let h =
        (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':'
    let m =
        (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes())

    return h + m
};

const timestampToDayHM = (timestamp) => {
    if(timestamp==null){
        return '-'
    }
    let date = new Date(timestamp) //时间戳为10位需*1000，时间戳为13位的话不需乘1000
    let Y = date.getFullYear() + '-'
    let M =
        (date.getMonth() + 1 < 10 ?
            '0' + (date.getMonth() + 1) :
            date.getMonth() + 1) + '-'
    let D =
        (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' '
    let h =
        (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':'
    let m =
        (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes())
    return Y + M + D + h + m
};

const calBetweenDays = (d1, d2) => {
    var t1=(new Date(d1)).getTime()
    var t2=(new Date(d2)).getTime()
    var diffInDays = Math.floor((t2 - t1) / (1000 * 3600 * 24))
    return diffInDays
};

const amountString = (val) => {
    if(!val||val==''){
        return '0'
    }
    val = val.toString().replace(/\$|\,/g,'');
    if(isNaN(val)) {
      val = "0";  
    } 
    let sign = (val == (val = Math.abs(val)));
    val = Math.floor(val*100+0.50000000001);
    let cents = val%100;
    val = Math.floor(val/100).toString();
    if(cents<10) {
       cents = "0" + cents
    }
    for (var i = 0; i < Math.floor((val.length-(1+i))/3); i++) {
        val = val.substring(0,val.length-(4*i+3))+',' + val.substring(val.length-(4*i+3));
    }
 
    return (((sign)?'':'-') + val + '.' + cents);
};

/**
 * 存储localStorage
 */
const setStore = (name, content) => {
    if (!name) return;
    if (typeof content !== 'string') {
        content = JSON.stringify(content);
    }
    window.localStorage.setItem(name, content);
}

/**
 * 获取localStorage
 */
const getStore = name => {
    if (!name) return;
    return window.localStorage.getItem(name);
}

/**
 * 删除localStorage
 */
const removeStore = name => {
    if (!name) return;
    window.localStorage.removeItem(name);
}

/**
 * 设置cookie
 **/
function setCookie(name, value, day) {
    let date = new Date();
    date.setDate(date.getDate() + day);
    document.cookie = name + '=' + value + ';expires=' + date;
};

/**
 * 获取cookie
 **/
function getCookie(name) {
    let reg = RegExp(name + '=([^;]+)');
    let arr = document.cookie.match(reg);
    if (arr) {
        return arr[1];
    } else {
        return '';
    }
};

/**
 * 删除cookie
 **/
function delCookie(name) {
    setCookie(name, null, -1);
};

//封装jsonp方法
function jsonp0(setting){
	setting.data = setting.data || {}
	setting.key = setting.key||'callback'
	setting.callback = setting.callback||function(){} 
 //指定jsonp的回调方法名称
	setting.data[setting.key] = 'jsonpcallback'
 // jsonp回调方法
	window.jsonpcallback = function(data){
	  setting.callback (data);
	}
	var script = document.createElement('script')
	var query = []
	for(var key in setting.data){
	  query.push( key + '='+ encodeURIComponent(setting.data[key]) )
	}
	script.src = setting.url + '&' + query.join('&')
	document.head.appendChild(script)
	document.head.removeChild(script)
}

function jsonp(url, params, callback) {
    var callbackName = 'jsonp_callback_' + Math.round(100000 * Math.random())
    window[callbackName] = function(data) {
    delete window[callbackName]
    document.body.removeChild(script)
    callback(data)
    }
    var queryString = ''
    for (var key in params) {
    if (params.hasOwnProperty(key)) {
    queryString += encodeURIComponent(key) + '=' + encodeURIComponent(params[key]) + '&'
    }
    }
    var script = document.createElement('script')
    script.src = url + '?' + queryString + 'callback=' + callbackName
    document.body.appendChild(script)
}

/**
 * 导出 
 **/
export {
    timestampToTime,
    timestampToDate,
    timestampToHM,
    timestampToDayHM,
    calBetweenDays,
    setStore,
    getStore,
    removeStore,
    setCookie,
    getCookie,
    delCookie,
    amountString,
    jsonp
}