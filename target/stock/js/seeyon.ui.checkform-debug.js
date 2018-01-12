/*
 * 使用本form验证组件，要求input存在validate属性，并且validate属性的值要求是一个合法的json值。 例如：<input
 * name="username" type="text"
 * validate="{type:'string',maxLength:20,minLength:6}"/> 以下所说的属性全部都是json字符串中的属性
 * 主属性是验证某一个类型必须存在的属性，副属性表示不是必须存在的属性。
 * 
 * 目前一共可以验证13种类型，再加上最后一种给定自定义正则表达式的自定义验证类型。
 * name属性或displayName在显示错误信息的时候要用到，但是也可以选择不给出（此时作为输入框的name属性必须存在）
 * 
 * 1、非空 主属性：notNull=true或nullable=true
 * 
 * 2、包含空格的非空 主属性：notNullWithoutTrim=true
 * 
 * 3、数字 主属性：isNumber=true或type=number或type=1或type=2
 * 副属性：max或maxValue（必须是数字，如果两个都给出，只使用前者） min或minValue（必须是数字，如果两个都给出，只使用前者）
 * integerDigits整数位数（必须是数字） decimalDigits或dotNumber小数位数（必须是数字，如果两个都给出，只使用前者）
 * 
 * 4、整形数字 主属性：isInteger=true 副属性：max或maxValue（必须是数字，如果两个都给出，只使用前者）
 * min或minValue（必须是数字，如果两个都给出，只使用前者）
 * 
 * 5、电子邮件 主属性：isEmail=true或type=email
 * 
 * 6、字符串 主属性：isWord=true或type=string或type=8或type=9
 * 副属性：character=""，将所有你认为是特殊字符的字符放进去，例如character="!@#$%^&*()"。（如果特殊字符中存在中划线-的话，必须放在第一个）
 * 
 * 7、不允许默认值 主属性：isDeaultValue=true、deaultValue=一个给定的值
 * 
 * 8、固定电话号码 主属性：type=telephone
 * 
 * 9、手机号码 主属性：type=mobilePhone
 * 
 * 10、日期类型（要求格式：yyyy-MM-dd） 主属性：type=3
 * 
 * 11、日期时间类型（要求各市：yyyy-MM-dd HH:mm:ss） 主属性：type=4
 * 
 * 12、最大长度 主属性：maxLength=一个数字
 * 
 * 13、最小长度 主属性：minLength=一个数字
 * 
 * 14、自定义正则表达式 主属性：regExp=一个自定义的正则表达式，例如/[\d+]/
 * 
 * 15、自定义校验函数
 * 主属性：func=一个自己定义的函数（必须是全局函数，可以是匿名函数）（该函数可以没有返回值（js中没有返回值的话默认为null），返回null或false表示校验未通过）
 * 校验的时候会将当前input作为自定义校验函数的第一个参数传入。
 * 
 * 16、自定义错误提示 主属性：errorMsg=一个字符串，例如:请输入数字！
 * 
 * 17、js设置校验规则
 */
(function() {
  // fieldType可能的值
  var fieldTypeValue = {
    "string" : "string"// 是否为一般字符串
    ,
    "number" : "number"// 是否是数字
    ,
    "email" : "email"// 是否是电子邮件
    ,
    "telephone" : "telephone"// 固定电话
    ,
    "mobilePhone" : "mobilePhone"// 手机
    ,
    "0" : "0"// 字符串（同string），表单模块专用
    ,
    "1" : "1"// 整数同number，表单模块转用，但是现在已不再用，统一用2，表示数字
    ,
    "2" : "2"// 小数同number，表单模块专用
    ,
    "3" : "3"// 日期类型 统一yy-MM-dd格式 表单模块专用
    ,
    "4" : "4"// 日期时间类型 统一yy-MM-dd HH:mm:ss 表单模块专用
    ,
    "5" : "5"// 日期时间类型 统一yy-MM-dd HH:mm 表单模块专用
    ,
    "8" : "0"// 图片类型同string
    ,
    "9" : "0"// 附件类型同string
    ,
    "6" : "6"
  }
  var fieldType = "type";// 输入值的类型
  var fieldName = "name";// 标签名字
  var maxValue = "maxValue";// 输入数字的最大值
  var minValue = "minValue";// 输入数字的最小值
  var minLength = "minLength";// 最小长度
  var maxLength = "maxLength";// 最大长度
  var china3char = "china3char";//字符串的长度取值中是否将中文认为是3个字符。
  var notNull = "notNull";// 是否可以为空true表示不可以为空，false或未定义表示可以为空
  var dotNumber = "dotNumber";// 小数点后允许的位数
  var fieldDiaplayName = "displayName";// 标签名字，表单模块专用，不是同fieldName,单元格的显示名称
  var fieldLen = "fieldLen";// 最大可输入长度，表单模块专用，同maxLength
  var nullable = "nullable";// 是否可以为空，表单模块专用，"0"可以为空,"1"不可以为空
  var isNumber = "isNumber";// 是否是数字
  var integerDigits = "integerDigits";// 整数数位，必须与isNumber一起使用，单独使用无意义
  var decimalDigits = "decimalDigits";// 小数位数，必须与isNumber一起使用，单独使用无意义
  var isEmail = "isEmail";// 是否是电子邮件
  var notNullWithoutTrim = "notNullWithoutTrim";// 验证是否为空，允许空格
  var isInteger = "isInteger";// 是否为整数
  var max = "max";// 最大值，与isNumber或isInteger或type=number一起使用
  var min = "min";// 最小值，与isNumber或isInteger或type=number一起使用
  var maxEqual = "maxEqual";//是否允许等于最大值
  var minEqual = "minEqual";//是否允许等于最小值
  var isWord = "isWord";// 是否为正常的字符串，不允许特殊字符，如：/
  var character = "avoidChar";// 指定的字符，与isWord共同使用，此时isWord必须不能为空。
  var isDeaultValue = "isDeaultValue";// 是否为默认值，如果是默认值，提示为空，与deaultValue共同使用，二者必须都不能为空
  var deaultValue = "deaultValue";// 默认值，与isDeaultValue共同使用，二者必须都不能为空
  var regExp = "regExp";// 自定义正则表达式
  var errorMsg = "errorMsg";// 自定义错误显示
  var func = "func";// 自定义校验函数
  
  $.fn.resetValidate = function(options) {
	  var errorClassName = 'error-form';
	  this.find('.'+errorClassName).removeClass(errorClassName).each(function(i,e) {
	      	var prt = $(e);
	      	var es = prt.data("errorIcon");
	      	if(es){
	            prt.removeClass('error-form').next().remove();
	            prt.removeAttr("title");
	            prt.removeData("errorIcon");
              prt.find('input').unbind('propertychange');
	              prt.css({
	                'width' : ($(this).width() + 20) + 'px'
	              });

            }     	
	      });
  };  
  /*
   * 校验一个form所有的输入框，校验规则由输入框的validate属性指定
   */
function checkValue(obj,options,errorArray){
	var resultTemp = obj;
    var resultTempPar = resultTemp.parent();
    curCheckObj = checkInput(resultTemp, options.checkNull);
    if (curCheckObj.errorArray) {
    	var len = curCheckObj.errorArray.length;
        if (len > 0) {
            showError(resultTempPar, curCheckObj,options,resultTemp);
        	if (!resultTempPar.hasClass('error-form')||resultTemp.attr("comptype")=="calendar") {
        		if(resultTemp.hasClass("comp")){//如果是日期类型，增加获取焦点验证  byxiexp
          	  		resultTemp.focus(function(){
				        checkval($(this),options);			           	
	        		})
	          	}
			}
			if($.browser.msie && parseInt($.browser.version)<9){
				resultTemp.bind("propertychange", function(e) {
					if(e.originalEvent && e.originalEvent.propertyName=="value"){
						checkval($(this),options);
					}
				})
			}else{
				resultTemp.bind("change",function(){
					checkval($(this),options);
				});
			}
          	errorArray.push(curCheckObj.errorArray.join("\r\n"));
        }
	}
}
  function MxtCheckForm(form, options) {
    // 判断传进来的是form标签的Id还是以一个表示form标签的Dom对象
    if (typeof form === 'string') {
      form = $("#" + form);
    } else {
      form = $(form);
    }
    // 得到该form下所有的可用、非隐藏的表单元素
    var errorArray = [], curCheckObj = null;
    var result = [];
	var focusObj = null;//第一个错误对象自动聚焦
	if(options && options.validateHidden){
		$(".validate", form).add(form).each(function() {
          if (!this.disabled && $(this).attr("validate") && this.tagName.toLowerCase()!="span" && this.tagName.toLowerCase()!="div")
            result.push($(this));
        });
	} else {
	    $(".validate", form).add(form).each(function() {
	      if (!this.disabled && $(this).is(':visible') && $(this).attr("validate") && this.tagName.toLowerCase()!="span" && this.tagName.toLowerCase()!="div")
	        result.push($(this));
	    });
	}
    
    for ( var i = 0; i < result.length; i++) {
      // 获取所有的属性值
	  if(i == 0){focusObj = result[0]}//第一个错误对象自动聚焦
      checkValue(result[i],options,errorArray);
      result[i] = null;
    }
    // 清空form变量
    form = null;
	if(focusObj!=null){
		//第一个错误对象自动聚焦
		//focusObj.focus();//会导致表单中的参与计算的字段选择后光标不能离开单元格 比如日期选择控件
		var tempId = focusObj.attr("id");
		if(tempId!=null){
			if(!/^field\d{0,5}/.test(tempId)){
				focusObj.focus();
			}
		}else{
			focusObj.focus();
		}
	}
    // 显示错误
    if (errorArray.length > 0) {
	  var errorAlert = options.errorAlert;
	  if(errorAlert){
	      if(top.$("#checkformError").length<=0){
	          $.alert({id:"checkformError",msg:errorArray.join("<br/>")});
	      }
	  }
      return false;
    } else {
      return true;
    }
  }
  /**
   * 校验单个输入框
   */
  function MxtCheckInput(input) {
  	 var options = {
      errorIcon : true
    };
    if (input == null) {
      return true;
    }
    var errorArray = [];
    checkValue(input,options,errorArray);  
    var result = true;
    if (curCheckObj.errorArray && curCheckObj.errorArray.length > 0) {
      result = false;
    }
    input = null;
    return result;
  }
  /*
   * 使用js添加校验信息
   */
  function MxtCheckMsg(msg, context) {
    if (msg == null)
      return;
    context = context || document.body;
    // 判断传进来的是form标签的Id还是以一个Dom对象
    if (typeof context === 'string') {
      context = $("#" + context);
    } else {
      context = $(context);
    }
    for ( var elemSelector in msg) {
      addCheckMsg($(elemSelector, context), msg[elemSelector]);
    }
    context = null;
  }
  window.MxtCheckForm = MxtCheckForm;
  window.MxtCheckInput = MxtCheckInput;
  window.MxtCheckMsg = MxtCheckMsg;
  window.RemoveCheckMsg = checkval;
  function checkInput(input, checkIsNull) {
    if (input == null) {
      return {};
    }
    var checkObj = getCheckObj(input);
    if (checkObj != null) {
      if (checkObj.errorArray == null) {
        checkObj.errorArray = [];
      }
      checkObj.checkNull = checkIsNull;
      if(checkIsNull){
	      checkNotNull(checkObj);
	      if(checkObj.errorArray.length>0){
            input = null;
            return checkObj;
          }
	      checkNotNullWithourTrim(checkObj);
	      if(checkObj.errorArray.length>0){
	      	input = null;
	      	return checkObj;
	      }
      }
      checkIsNumber(checkObj);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
      checkIsEmail(checkObj);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
      checkIsWord(checkObj);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
      checkIsDefaultValue(checkObj);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
      checkIsTelephone(checkObj);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
      checkIsMobilePhone(checkObj);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
      checkIsDate(checkObj);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
      checkIsDateTime(checkObj);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
      checkIsDateTimes(checkObj);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
      checkIsShorterThanMax(checkObj);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
      checkIsLongerThanMin(checkObj);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
      checkCustomRegExp(checkObj);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
      checkCustomFunc(checkObj, input);
      if(checkObj.errorArray.length>0){
        input = null;
        return checkObj;
      }
    }
    input = null;
    return checkObj;
  }
  function checkval(obj,options){
  		if(options==null){
	  		var settings = {
		      errorIcon : true,
			  errorAlert:false,
			  errorBg:false,
			  validateHidden:false,
			  checkNull:true
		    };
		    options = $.extend(settings, options);
  		}
    	var temp = checkInput(obj, true);
	      var prt = obj.parent();
	      if (!temp.errorArray || (temp.errorArray && temp.errorArray.length === 0)) {
	        prt.removeClass('error-form');
	        prt.removeAttr("title");
	        if (prt.data("errorIcon")) {
	          prt.removeData("errorIcon");
	          prt.next().remove();
	          if (options.errorIcon) {
	            prt.css({
	              'width' : (prt.width() + 20) + 'px'
	            });
	            if(obj.hasClass("comp")) obj.width("100%");//如果是日期类型，input加20像素的宽度 by xiexp
	          }
	        }
			if (prt.data("errorBg")) {
				prt.removeData("errorBg");
				prt.css('background','#fff');
				if(obj && !(temp[notNull] || temp[nullable]) ){$(obj).css('background','#fff');}
			}
	      } 
	      else {
	        showError(prt, temp,options,obj);
	        //if(obj.hasClass("comp")) obj.width(obj.width()-20+"px");//如果是日期类型，input加20像素的宽度 by xiexp
	      }
    }
    function showError(par, obj,options,objDom) {
			if(options){
		    	var _w = par.width();
				if(options.errorIcon){
			        par.addClass('error-form').css({
			          'float' : 'left'
			        }).attr("title", obj.errorArray.join(""));
			        var es = par.data("errorIcon");
			        if (!es) {
			            es = $("<span class='error-title'></span>");
			            par.data("errorIcon", es);
			            par.after(es);
			            par.css({
			              'width' : (_w - 20) + 'px'
			            });
			        }
					es.attr("title", obj.errorArray.join(""));
				}
				if(options.errorBg){
			        var bg = par.data("errorBg");
			        if (!bg) {
						par.css('background','#FCDD8B');
						if(objDom)$(objDom).css('background','#FCDD8B');
			            par.data("errorBg", true);
			        }
				}
			}else{
	          //par.css({
	          //  'width' : _w + 'px'
	          //});
			}
      }    
  /*
   * 修改单个输入框的校验信息
   */
  function addCheckMsg(elem, msg) {
    if (elem == null) {
      return;
    }
    if (typeof msg == 'string') {
        try {
            msg = eval("({" + msg + "})");
        } catch (e) {
            return;
        }
    }
    var checkObj = getCheckObj(elem);
    $.extend(checkObj, msg);
    elem = null;
  }
  /*
   * 怎么获取form上面一个input的验证对象？我期待该input的validate属性值是一个合法的json字符串，否则的话就得到一个null对象。
   */
  function getCheckObj(input) {
    if (input == null) {
      return {};
    }
    if (input.data("checkObj")) {
      var checkObj = input.data("checkObj");
      checkObj.errorArray = null;
      checkObj.value = input.val();
      return input.data("checkObj");
    }
    var checkObj = null, validate = null;
    input = $(input);
    validate = input.attr("validate");
    if (validate == null || $.trim(validate) == "") {
      return {};
    } else {
      try {
        checkObj = eval("({" + validate + "})");
      } catch (e) {
        checkObj = {};
        var errorMessage = input.attr("name") + $.i18n('validate.notJson.js');
        addErrorMessage(checkObj, errorMessage, true);
        input = null;
        return checkObj;
      }
    }
    if (checkObj.name == null) {
      var tempName = checkObj[fieldName] || checkObj[fieldDiaplayName]
          || input.attr("name");
      if (tempName) {
        checkObj.name = tempName;
      } else {
        var errorMessage = $.i18n('validate.notName.js');
        addErrorMessage(checkObj, errorMessage, true);
        input = null;
        return checkObj;
      }
    }
    checkObj.value = input.val();
    input.data("checkObj", checkObj);
    input = null;
    return checkObj;
  }
  /*
   * 怎么判断是否为空？同时判断它的额外属性呢？ 先判断notNull、nullable属性是不是为真，为真的话再来判断
   * 再判断maxLength属性是不是为空，不为空的话再来判断是不是大于最大程度
   */
  function checkNotNull(obj) {
    if (obj == null) {
      return true;
    }
    if (obj[notNull] || obj[nullable]) {
      if (isNull(obj.value)) {
        addErrorMessage(obj, obj.name
            + $.i18n('validate.notNull.js'), true);
        return false;
      }
    }
    return true;
  }
  /*
   * 怎么判断是否为空？同时判断它的额外属性呢？ 先判断notNull、nullable属性是不是为真，为真的话再来判断
   * 再判断maxLength属性是不是为空，不为空的话再来判断是不是大于最大程度
   */
  function checkNotNullWithourTrim(obj) {
    if (obj == null) {
      return true;
    }
    if (obj[notNullWithoutTrim]) {
      if (isNull(obj.value, true)) {
        addErrorMessage(obj, obj.name + $.i18n('validate.notNull.js'), true);
        return false;
      }
    }
    return true;
  }
  /*
   * 如果是空的话，判定它也是一个数字 判断是否是数字简单，判断整数数位和小数数位有点繁琐
   * 小数点所在的索引就是整数位数，字长-小数点所在的索引-1就是小数位的位数。
   */
  /*
   * 如果为空的话，判定它也是一个数字 判断是否是整数比较简单，只要是一个数字，然后又没有小数点就行了。 然后及时判断最大值，最小值了。
   */
  function checkIsNumber(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    var isNumberFlag = false, isIntegerFlag = false;
    if (obj[isNumber] || (obj[fieldType] == fieldTypeValue["number"])
        || (obj[fieldType] == fieldTypeValue["1"])
        || (obj[fieldType] == fieldTypeValue["2"])) {
      isNumberFlag = true;
      if (isNull(obj.value)) {
        return true;
      }
      if (!isANumber(obj.value)) {
        addErrorMessage(obj, obj.name + $.i18n('validate.notNumber.js'));
        return false;
      }
      // 额外属性
      var value = "" + $.trim(obj.value);
      var dotIndex = value.indexOf("."), intdigits = obj[integerDigits], decDigits = obj[decimalDigits];
      if(decDigits==null){ decDigits = obj[dotNumber];}
      if (intdigits!=null && isANumber(intdigits)) {
        var intbits = dotIndex > -1 ? dotIndex : value.length;
        if (intbits > parseInt(intdigits)) {
          addErrorMessage(obj, obj.name +  $.i18n('validate.integerDigits.js', intdigits));
          return false;
        }
      }
      if (decDigits!=null && isANumber(decDigits)) {
        var decbits = dotIndex > -1 ? (value.length - 1 - dotIndex) : 0;
        if (decbits > parseInt(decDigits)) {
          addErrorMessage(obj, obj.name + $.i18n('validate.decimalDigits.js', decDigits));
          return false;
        }
      }
    }
    if (obj[isInteger]) {
      isIntegerFlag = true;
      if (isNull(obj.value)) {
        return true;
      }
      // 如果是数字，同时没有点号存在，表示是一个整数
      if (!isANumber(obj.value)) {
        addErrorMessage(obj, obj.name + $.i18n('validate.integer.js'));
        return false;
      }
      var value = "" + obj.value;
      if (value.indexOf(".") > -1) {
        addErrorMessage(obj, obj.name
            + $.i18n('validate.integer.decimal.js'));
        return false;
      }
    }
    if(isNumberFlag || isIntegerFlag){
	    if (!checkIsLessThanMax(obj)) {
	      return false;
	    }
	    if (!checkIsBiggerThanMin(obj)) {
	      return false;
	    }
    }
    return true;
  }
  /*
   * 为空的话判定它是一个电子邮箱。 怎么判断是一个电子邮箱？
   * 存在@符号，并且@不能是第一个字符，不能在前五个字符中间，必须存在点号，点号不能在最后两个字符之中
   */
  function checkIsEmail(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    if (obj[isEmail] || (obj[fieldType] == "email")) {
      var str = obj.value;
      if (isNull(str)) {
        return true;
      } else {
        var result = true;
        if (str.indexOf("@") == -1) {
          result = false;
        } else if (str.indexOf("@") == 0) {
          result = false;
        } else if ((str.length - str.indexOf("@")) < 5) {
          result = false;
        } else if (str.indexOf(".") == -1) {
          result = false;
        } else if ((str.length - str.indexOf(".")) < 3) {
          result = false;
        }
        if (!result) {
          addErrorMessage(obj, obj.name
              + $.i18n('validate.email.js'));
        }
        return result;
      }
    }
    return true;
  }
  /*
   * 判断是不是一个合法的字符串 只要typeof的值是一个string就行。
   * obj[character]中包含的是非法字符，如果字符串中包含有一些非法字符，那么验证不会通过。
   */
  function checkIsWord(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    if (obj[isWord] || (obj[fieldType] == fieldTypeValue["string"])
        || (obj[fieldType] == fieldTypeValue["0"])
        || (obj[fieldType] == fieldTypeValue["8"])
        || (obj[fieldType] == fieldTypeValue["9"])) {
      if (typeof obj.value != "string") {
        return false;
      }
      if (obj[character] && typeof obj[character]=="string") {

        var value = "" + obj.value, chars = obj[character];
        for(var i=0,len=chars.length; i<len; i++){
            if (value.indexOf(chars.charAt(i)) > -1) {
              addErrorMessage(obj, obj.name + $.i18n('validate.specialhave.js')
                  + obj[character] + "!");
              return false;
            }
        }
      }
    }
    return true;
  }
  /*
   * 判断是否是默认值 如果为空的话，判定为不是 太简单了，只要判断是二者是否相等就行了。
   */
  function checkIsDefaultValue(obj) {
    if (obj == null) {
      return false;
    }
    if (isNull(obj.value)) {
      return false;
    }
    if (obj[isDeaultValue] && obj[deaultValue]) {
      if ($.trim(obj.value) == obj[deaultValue]) {
        addErrorMessage(obj, obj.name
            + $.i18n('validate.notDefault.js')+"("
            + obj[deaultValue] + ")!");
        return true;
      }
    }
    return false;
  }
  /*
   * 判断是否是固定电话号码 如果为空的话，判定为true 以数字开头，数字结尾，中间不能是0-9及-_意外的字符
   */
  var telReg = /^\d[-_0-9]{5,}\d$/;
  function checkIsTelephone(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    if (obj[fieldType] == "telephone") {
      var value = "" + obj.value;
      if (telReg.test(value) != true) {
        addErrorMessage(
            obj,
            obj.name
                + $.i18n('validate.phoneNumber.js'));
        return false;
      }
    }
    return true;
  }
  /*
   * 判断是不是手机号也很简单 如果是空的话，判定它是手机号码 判断是不是至少11为手机号码就行了。
   */
  var mobileReg = /\d{11,}/;
  function checkIsMobilePhone(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    if (obj[fieldType] == "mobilePhone") {
      var value = "" + obj.value;
      if (mobileReg.test(value) != true) {
        addErrorMessage(obj, obj.name
            + $.i18n('validate.mobileNumber.js'));
        return false;
      }
    }
    return true;
  }
  /*
   * 判断是不是日期格式的话，很简单 如果是空的话，判定它是日期 如果不是空，那么判断它是不是符合正则表达式就行了
   */
  var dateReg = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29))$/;
  function checkIsDate(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    if (obj[fieldType] == 3) {
      var value = "" + obj.value;
      if (dateReg.test(value) != true) {
        addErrorMessage(obj, obj.name
            + $.i18n('validate.yyyy.MM.dd.js'));
        return false;
      }
    }
    return true;
  }
  /*
   * 判断是不是日期时间格式的话，也很简单 如果是空的话，判定它是日期 如果不是空，那么判断它是不是符合正则表达式就行了
   */
  var datetimeReg = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29)) (20|21|22|23|[0-1]?\d):[0-5]?\d:[0-5]?\d$/;
  function checkIsDateTime(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    if (obj[fieldType] == 4) {
      var value = "" + obj.value;
      if (datetimeReg.test(value) != true) {
        addErrorMessage(obj, obj.name
            + $.i18n('validate.yyyy.MM.dd.hh.mm.ss.js'));
        return false;
      }
    }
    return true;
  }
  /*
   * 判断是不是日期时间格式的话，也很简单 如果是空的话，判定它是日期 如果不是空，那么判断它是不是符合正则表达式就行了
   */
  var datetimeRegs = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29)) (20|21|22|23|[0-1]?\d):[0-5]?\d$/;
  function checkIsDateTimes(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    if (obj[fieldType] == 5) {
      var value = "" + obj.value;
      if (datetimeRegs.test(value) != true) {
        addErrorMessage(obj, obj.name
            + $.i18n('validate.yyyy.MM.dd.hh.mm.ss.js'));
        return false;
      }
    }
    return true;
  }
  /*
   * 判断是不是小于允许的最大长度
   */
  function checkIsShorterThanMax(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    if (obj[maxLength] != null) {
      if (isANumber(obj[maxLength])) {
      	//
		if(obj[isNumber] || (obj[fieldType] == fieldTypeValue["number"])
				|| (obj[fieldType] == fieldTypeValue["1"])
				|| (obj[fieldType] == fieldTypeValue["2"])){
			if(obj.value.length>0){
				obj.value = obj.value.replace(/\./,'');
			}
      		if (longerThanSecond(obj.value, obj[maxLength])) {
				  addErrorMessage(obj, obj.name
					  + $.i18n('validate.maxLength.js', obj[maxLength]));
				  return false;
			}
      	}else {
			if(obj[china3char]==true){
				//如果这个参数设置为true，表示非英文字符应该当作3个字符。否则只算字符的个数。
				if (longerThanSecondChina(obj.value, obj[maxLength])) {
				  addErrorMessage(obj, obj.name
					  + $.i18n('validate.maxLengthChina.js', obj[maxLength]));
				  return false;
				}
			}else{
				if (longerThanSecond(obj.value, obj[maxLength])) {
				  addErrorMessage(obj, obj.name
					  + $.i18n('validate.maxLength.js', obj[maxLength]));
				  return false;
				}
			}
		}
      } else {
        addErrorMessage(obj, obj.name
            + $.i18n('validate.maxLength.notNumber.js'));
        return false;
      }
    }
    return true;
  }
  /*
   * 判断是否大于允许的最小长度
   */
  function checkIsLongerThanMin(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    if (obj[minLength] != null) {
      if (isANumber(obj[minLength])) {
        if (shorterThanSecond(obj.value, obj[minLength])) {
          addErrorMessage(obj, obj.name
              + $.i18n('validate.minLength.js') + obj[minLength]
              + "!");
          return false;
        }
      } else {
        addErrorMessage(obj, obj.name
            + $.i18n('validate.minLength.notNumber.js'));
        return false;
      }
    }
    return true;
  }
  /*
   * 判断是否小于最大值
   */
  function checkIsLessThanMax(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    var tempMax = obj[max] ;
    if(tempMax==null){tempMax=obj[maxValue]};
    if (tempMax != null && isANumber(tempMax)) {
      value = parseFloat(obj.value);
      if(obj[maxEqual]==false){
      	if (tempMax <= value) {
	        addErrorMessage(obj, obj.name + $.i18n('validate.mustLittle.js') + tempMax
	            + "!");
	        return false;
	      }
      }else{
	      if (tempMax < value) {
	        addErrorMessage(obj, obj.name + $.i18n('validate.mustLittleOrEqual.js') + tempMax
	            + "!");
	        return false;
	      }
      }
    }
    return true;
  }
  /*
   * 判断是否大于最小值
   */
  function checkIsBiggerThanMin(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    var tempMin = obj[min];
    if(tempMin==null){tempMin = obj[minValue]};
    if (tempMin != null && isANumber(tempMin)) {
      value = parseFloat(obj.value);
      if(obj[minEqual]==false){
      	  if (tempMin >= value) {
	        addErrorMessage(obj, obj.name + $.i18n('validate.mustBigger.js')
	            + tempMin + "!");
	        return false;
	      }
      }else{
	      if (tempMin > value) {
	        addErrorMessage(obj, obj.name + $.i18n('validate.mustBiggerOrEqual.js')
	            + tempMin + "!");
	        return false;
	      }
      }
    }
    return true;
  }
  /*
   * 判断输入值是否匹配用户自定义的正则表达式
   */
  function checkCustomRegExp(obj) {
    if (obj == null) {
      return true;
    }
    if (isNull(obj.value)) {
      return true;
    }
    var customRegExp = obj[regExp];
    if (customRegExp != null) {
      if (!new RegExp(customRegExp).test(obj.value)) {
        addErrorMessage(obj, obj.name + $.i18n('validate.notMatch.js'));
        return false;
      }
    }
    return true;
  }
  function checkCustomFunc(obj, input) {
    if (obj == null || input == null) {
      input = null;
      return true;
    }
    if (typeof obj[func] == "function") {
      var result = obj[func](input, obj);
      if (result == null || result == false) {
        addErrorMessage(obj, obj.name + $.i18n('validate.notMatch.js'));
      }
    }
    input = null;
    return true;
  }
  /*
   * 判断是否为空。 null的话，返回true。 是字符串的话，等于空字符串的话返回true，不是空返回false 以上条件都不满足，返回false
   */
  function isNull(value, notTrim) {
    if (value == null) {
      return true;
    } else if (typeof (value) == "string") {
      value = notTrim == true ? $.trim(value) : value;
      if (value == "") {
        return true;
      }
    }
    return false;
  }
  /*
   * isNaN返回值为true的话，表明不是数字。返回值为false的话表明是数字。
   */
  function isANumber(value) {
    if (typeof value == "string") {
      value = value;
    }
	return /^[-+]?\d+([\.]\d+)?$/.test(value);
    /*if (isNaN(value)) {
      return false;
    } else {
      return true;
    }*/
  }
  /*
   * 两个参数都不能为空，只要有一个为空，就返回false 第二个参数必须为数字，不是数字的话，就返回false
   * 以上条件都满足，那么如果first的长度大于second的值，就返回true 以上条件都不满足，然会false
   */
  function longerThanSecond(first, second) {
    if (first != null && second != null && isANumber(second)) {
      second = parseFloat(second);
      if (first.length > second) {
        return true;
      }
    }
    return false;
  }
  function longerThanSecondChina(first, second) {
  	//该方法的校验里非英文字符全部当作3个字符处理
    if (first != null && second != null && isANumber(second)) {
      second = parseFloat(second);
      if (getLength(first) > second) {
        return true;
      }
    }
    return false;
  }/*
     * 两个参数都不能为空，只要有一个为空，就返回false 第二个参数必须为数字，不是数字的话，就返回false
     * 以上条件都满足，那么如果first的长度大于second的值，就返回true 以上条件都不满足，然会false
     */
  function shorterThanSecond(first, second) {
    if (first != null && second != null && isANumber(second)) {
      second = parseFloat(second);
      if (first.length < second) {
        return true;
      }
    }
    return false;
  }
  function getLength(value){
  	if(value==null){
  		return 0;
  	}else if(value==""){
  		return 1;
  	}else{
  		var result = 0;
  		for(var i=0, len=value.length; i<len; i++){
  			var ch = value.charCodeAt(i);
  			if(ch<256){
  				result++;
  			}else{
  				result +=3;
  			}
  		}
  		return result;
  	}
  }
  /*
   * 添加错误信息
   */
  function addErrorMessage(obj, errMsg, useSystem) {
    if (obj) {
      if (obj.errorArray == null) {
        obj.errorArray = [];
      }
      var customErrorMsg = obj[errorMsg];
      if (useSystem!=true && customErrorMsg) {
        obj.errorArray.push(customErrorMsg);
      } else {
        obj.errorArray.push(errMsg);
      }
    }
  }

  $.fn.validate = function(options) {
    var settings = {
      errorIcon : true,
	  errorAlert:false,
	  errorBg:false,
	  validateHidden:false,
	  checkNull:true
    };
    options = $.extend(settings, options);
    return MxtCheckForm(this, options);
  };
  
  $.fn.validateChange = function (obj){
  	var tempElem = this;
  	if(tempElem!=null && tempElem.size()>0){
        addCheckMsg(tempElem, obj);
  	}
  	tempElem = null;
  }

  var invalid = [];
  $._invalidObj = function(obj) {
    if (obj)
      invalid.push(obj);
    invalid.contains(obj);
  };

  $._isInValid = function(obj) {
  	if(invalid.contains){
      return invalid.contains(obj);
    }
    return null;
  };
  $.isNull = isNull;
  $.isANumber = isANumber;
})();