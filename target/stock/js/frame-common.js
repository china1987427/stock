/**
 * @author macj
 */
//引入v3x.js及相关国际化js文件
//返回个人空间首页
function backToPersonalSpace(){

}
//更新个人空间链接地址
function updatePersonalSpaceByUrl(path,newLink){

}
//更新个人空间链接地址,并返回个人空间--领导空间
function updatePersonalSpaceURL(newLink){

}
//返回到当前选择的空间首页
function back(){
  var spaces = $.ctx.space;
  if(spaces){
    for (var i = 0; i < spaces.length; i++) {
      if(currentSpaceId == spaces[i][0]){
        showSpace(i,spaces[i][0],spaces[i][2]);
      }
    }
  }
}
//重新排列空间菜单，个人空间设置更改排序后调用
function realignSpaceMenu(accountId){

}
function reFlesh(){
  var mainsrc = $("#main").attr("src");
  if(mainsrc.indexOf(".psml") > 0){
	  $("#main").attr("src",mainsrc);
  }else{
	  document.getElementById("main").contentWindow.location.reload();
  }
  if(isAdmin != "true"){
    timeLineObjReset(timeLineObj);
  }
}
//开始进度条
var commonProgressbar = null;
function startProc(title){
  try{
    var options={
      text:title
    };
    if(title == undefined){
      options = {};
    }
    if(commonProgressbar!=null){
      commonProgressbar.start();
    }else{
        commonProgressbar = new MxtProgressBar(options);
    }
  }catch(e){}
}
//结束进度条
function endProc(){
  try{
    if(commonProgressbar)commonProgressbar.close();
    commonProgressbar = null;
  }catch(e){}
}
/**
* 重新载入菜单
*/
function reloadMenu(path, type){

}
//后台管理界面　返回首页
function backToHome(){
  //window.location.href = _ctxPath+"/main.do?method=main";
  $("#main").attr("src",_ctxPath+"/portal/portalController.do?method=showSystemNavigation");
}
function showContentPage(count){

}
var showLogoutMsgFlag = true;
function showLogoutMsg(msg){
  if(showLogoutMsgFlag){
    try{
      showLogoutMsgFlag = false;
      alert(msg);
      window.location.href = _ctxPath+"/main.do?method=logout";
    }
    catch(e){
      showLogoutMsgFlag = true;
    }
  }
}
//后退按钮事件
function historyBack(url){
  showLeftNavigation();
  document.getElementById('main').contentWindow.history.back();
}

//前进按钮事件
function historyForward(){
  showLeftNavigation();
  document.getElementById('main').contentWindow.history.forward();
}
//显示当前位置
function showLocation(html){
  var div = $("#nowLocation");
  //$('#content_layout_body_left_content').addClass('border_all');
  if(div){
    $(div).html(html);
    $(div).show();
    $('#nowLocationDiv').show();
    $("#main_div").css("top","20px");
  	if ($.browser.msie) {
  		if ($.browser.version == '6.0' || $.browser.version == '7.0') {
  			var isShowLoaction = $('#main').attr('isShowLoaction');
  			if(isShowLoaction == undefined || isShowLoaction == 'false'){
  			   $('#main').attr('isShowLoaction','true');
  				 //$('#main').height($('#main').height()-20);
  			}
  		}
		}
  }
}
function showMainBorder(){
  //$('#content_layout_body_left_content').addClass('border_all');
}
function hideMainBorder(){
  $('#content_layout_body_left_content').removeClass('border_all');
}
//隐藏当前位置
function hideLocation(){
  var div = $("#nowLocation"); 
  //人员卡片点击发送协同，调用hideLocation
  if($('.mask').size()==0){
    $('#content_layout_body_left_content').removeClass('border_all');
  }
  if(div){
    $(div).html("");
    $(div).hide();
    $('#nowLocationDiv').hide();
    $("#main_div").css("top",0);
  	if ($.browser.msie) {
  		if ($.browser.version == '6.0' || $.browser.version == '7.0') {
  			var isShowLoaction = $('#main').attr('isShowLoaction');
  			if(isShowLoaction == 'true'){
  			   $('#main').attr('isShowLoaction','false');
  				 //$('#main').height($('#main').height()+20);
  			}
  		}
		}
  }
}
function showLeftNavigation(){
  $("#main_layout_left").width(130);
  $("#frount_nav_div").show();
  $("#content_layout").css({'margin-left':""});
  $('#closeopenleft').removeClass('openLeft').addClass('closeLeft').show();
}
function hideLeftNavigation(){
  $("#main_layout_left").width(10);
  $("#frount_nav_div").hide();
  $("#content_layout").css({'margin-left':"5px"});
  $('#closeopenleft').removeClass('closeLeft').addClass('openLeft');
}
function showTimeLine(){
  $('#content_layout_body_right').show();
  $('#content_layout_body_left_content').css('marginRight',48);
  timeLineObjReset(timeLineObj);
}
function hideTimeLine(){
  $('#content_layout_body_right').hide();
  $('#timeLine_time_line_date_set_tooltip').hide();
  $('#content_layout_body_left_content').css('marginRight',10);
}
function showMainMenu(){
  $('#menuDiv').show();
  $('#content_layout_top').height(60);
  $('#content_layout_body').css('top',60);
}
function hideMainMenu(){
  $('#menuDiv').hide();
  $('#content_layout_top').height(0);
  $('#content_layout_body').css('top',0);
}
function hideLogo(){
  $('#logo').remove();
  $('#accountNameDiv').remove();
  $('#accountSecondNameDiv').remove();
}
function hideLogoutButton(){
  $("#logout").hide();
}
function showCurrentPage(pagePath){
  $("#main").attr("src",pagePath);
}
function setonbeforeunload(){
  try{
   var _mainsrc = $('#main').attr('src');
   if(_mainsrc != ''){
     var func = document.getElementById("main").contentWindow.onbeforeunload;
     var _fun = function(){
       if(func!=null) func();
       getCtpTop().$('.mask').remove();
	   var _dialog_box = getCtpTop().$('.dialog_box');
	   if(_dialog_box.size()>0){
		   var _iframes = getCtpTop().$('.dialog_box .dialog_main_content iframe');
		   if(_iframes.size()>0){
			   for(var k=0;k<_iframes.size();k++){
					_iframes[k].contentWindow.document.write('');
					_iframes[k].contentWindow.close();
			   }
			 _iframes.remove();
		   }
		_dialog_box.remove();
	   }
       getCtpTop().$('.shield').remove();
       getCtpTop().$('.mxt-window').remove();
	}
 	if(document.all){
 		document.getElementById("main").contentWindow.attachEvent('onbeforeunload', _fun);
	}else{
		document.getElementById("main").contentWindow.addEventListener("beforeunload",_fun,false);
	}
   }
  }catch(e){}
}
function removeAllWindow(){
	try{
	    getCtpTop().$('.mask').remove();
		   var _dialog_box = getCtpTop().$('.dialog_box');
		   if(_dialog_box.size()>0){
			   var _iframes = getCtpTop().$('.dialog_box .dialog_main_content iframe');
			   if(_iframes.size()>0){
				   for(var k=0;k<_iframes.size();k++){
						_iframes[k].contentWindow.document.write('');
						_iframes[k].contentWindow.close();
				   }
				 _iframes.remove();
			   }
			_dialog_box.remove();
		   }
	    getCtpTop().$('.shield').remove();
	    getCtpTop().$('.mxt-window').remove();
	}catch(e){}
}
function refreshShortcuts(){
  new portalManager().getCustomizeShortcutsOfMember({
    success : function(shortcuts){
      if(shortcuts){
        $.ctx.shortcut = shortcuts;
        $.initMyShortcut(shortcuts);
      }
    }
  });
}
function refreshMenus(){
  new portalManager().getCustomizeMenusOfMember({
    success : function(menus){
      if(menus){
        $.ctx.menu = menus;
        $.pageMenu(menus);
      }
    }
  });
}
function refreshNavigation(redirectSpaceId){
  new portalManager().getSpaceSort({
    success : function(space){
      if(space){
        $.ctx.space = space;
        $.initSpace(space,null,redirectSpaceId);
      }
    }
  });
}
function initSpaceNavigationNoDisplay(){
  var spaceSort= new portalManager().getSpaceSort({
    success : function(space){
      if(space){
        $.ctx.space = space;
        $.initSpace(space,null,null,true);
      }
    }
  });
}
function refreshAccountName(accountName,secondName){
  new portalManager().getPortalHospots($.ctx.template[0].id,{
    success : function(data){
      getCtpTop().$.ctx.template = $.parseJSON(data);
      refreshCtxHotSpotsData();
      getCtpTop().$.initHotspots();
    }
	});
}
function refreshHomePageForNC(){
  isOpenCloseWindow = false;
  window.location.href = _ctxPath+"/main.do?method=main&currentSpaceForNC="+currentSpaceId;
}
function changeFunction(ch){
  var result = "";
  switch(ch){
    case '（':{
      result = '︵';
      break;
    }
    case '）':{
      result = '︶';
      break;
    }
    case '{':{
      result = '︷';
      break;
    }
    case '}':{
      result = '︸';
      break;
    }
    case '<':{
      result = '︿';
      break;
    }
    case '>':{
      result = '﹀';
      break;
    }
    case '(':{
      result = '︵';
      break;
    }
    case ')':{
      result = '︶';
      break;
    }
    case '《':{
      result = '︽';
      break;
    }
    case '》':{
      result = '︾';
      break;
    }
    case '【':{
      result = '︻';
      break;
    }
    case '】':{
      result = '︼';
      break;
    }
  }
  return result;
}

//空间名称字符串替换
function replaceSpaceName(spaceName){
  spaceName = spaceName.replace(/(（|）|\{|\}|<|>|\(|\)|《|》|【|】)/g,changeFunction);
  return spaceName;
}
function saveWaitSendSuccessTip(content){
  var htmlContent="<div id='saveTip' class='align_center over_auto padding_5' style='color:#ffffff;width:130px; z-index:900; background-color:#85c93a;'>"+content+"</div>";
  var _left=($("#content_layout_body").width()-130)/2;
  var _top=$("#content_layout_body").offset().top;
  var panel = $.dialog({
      id:'saveTip',
      width: 140,
      height: 24,
      type: 'panel',
      html: htmlContent,
      left:_left,
      top:_top,
      shadow:false,
      panelParam:{
          show:false,
          margins:false
      }
  });
  setTimeout(function(){
      panel.close();
  },2000)
}
function getMainMenuObj(menu,_mainMenuId,_clickMenuId){
  //alert(_mainMenuId+"==="+_clickMenuId)
  var _menu = null;
  var _menu2 = null;
  var _menu3 = null;
  var _menuCurrent = null;
  
  var step;
  
  for (var j=0; j<menu.length; j++) {
    var _temp = menu[j];
    if(_temp.id == _mainMenuId){
      _menu = _temp;
      step = 1;
      break; 
    }
  };
  //alert(_menu.name)
  if(_menu!=null && _menu.items){
    for (var h=0; h<_menu.items.length; h++) {
      var _temp = _menu.items[h];
      if(_temp.id == _clickMenuId){
        _menuCurrent = _temp;
        step = 2;
        break;
      }else{
        if(_temp.items){
          for (var t=0; t<_temp.items.length; t++) {
            var _tempTemp = _temp.items[t];
            if(_tempTemp.id == _clickMenuId){
              _menuCurrent = _tempTemp;
              step = 3;
              break;
            }
          }
        }
      }
    }
  }
  
  if(_menuCurrent!= null){
    return {'menuObj':_menuCurrent,'step':step};
  }else{
    return null;
  }
  
}


var locationCount = 0;
var onbeforunloadFlag = true;
function reloadPage(id,currentId){
	return;
  //ie 6 7 计数  空间第一个 全刷 
  if(locationCount == 10){
    locationCount = 0;//count重置
    spaceReloadPage(id,currentId,"true");
  }
  locationCount++;
}
function spaceReloadPage(id,currentId,shortCutId){
	try{
		if(winUC || winIM){
			return
		}
	}catch(e){}
  if ($.browser.msie) {
    //if ($.browser.version == '6.0' || $.browser.version == '7.0') {
      isDirectClose = false;
      onbeforunloadFlag = false;
      try{
    	  window.location.href = _ctxPath+"/main.do?method=main&mainMenuId="+currentId+"&clickMenuId="+id+"&mainSpaceId=&shortCutId="+shortCutId;
      }catch(e){}
    //}  
  }
}
function winopen(url){
  var targeturl = url;
  if(LoginOpenWindow){
    var isMSIE = (navigator.appName == "Microsoft Internet Explorer");
    if(isMSIE){
      var newwin = window.open('', '', 'resizable=true,status=no');
      try{
        newwin.focus();
      }
      catch(e){
        alert("您打开了窗口拦截的功能，请先关闭该功能。 \n请点击首页的《辅助程序安装》。");
        self.history.back();
        return;
      }
            var _subHeight = 25;
            var sUserAgent = navigator.userAgent;
            var isWinVista = sUserAgent.indexOf("Windows NT 6.0") > -1 || sUserAgent.indexOf("Windows Vista") > -1;
            var isWin7 = sUserAgent.indexOf("Windows NT 6.1") > -1 || sUserAgent.indexOf("Windows 7") > -1;
            var isWin8 = sUserAgent.indexOf("Windows NT 6.1") > -1 || sUserAgent.indexOf("Windows 8") > -1;
            if(isWinVista || isWin7 || isWin8){
              _subHeight = 38;
            }
      if(newwin != null && document.all){
        newwin.moveTo(0, 0);
        newwin.resizeTo(screen.width, screen.height - _subHeight);
      }
  
      newwin.location.href = targeturl;
      closeIt();
    }
    else{
      window.open (targeturl, '', 'height='+window.screen.height+', width='+window.screen.width+', top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no')
      window.close();
    }
  }
  else{
    self.location.href = targeturl;
  }
}

function closeIt(){
  var nAppName = navigator.appName;
  var nAppVersion = navigator.appVersion;

  if(nAppName=="Netscape"){
    nVersionNum  = nAppVersion.substring(0,2);
  }
  else{
    var startPoint = nAppVersion.indexOf("MSIE ")+5;
    nVersionNum = nAppVersion.substring(startPoint,startPoint+3);
  }
  
  try{
    if(nVersionNum >= 7){
      window.open("closeIE7.htm", "_self");
    }
    else if(nVersionNum > 5.5){
      window.opener = null;
      window.close();
    }
    else{//IE5.5以下的
      document.write("<object classid=clsid:adb880a6-d8ff-11cf-9377-00aa003b7a11 id=closes><param name=Command value=Close></object>");
      closes.Click();
    }
  }
  catch(e){
  }
}

/**
 * 协同调用外框架方法
 * @param objJson json对象
 * @return
 */
function callTopFrameMethod(objJson){
    //新建会议
    if (objJson.openPage == "createMeeting") {
        if (typeof(objJson.frameObj.dialogDealColl)!='undefined' && objJson.frameObj.dialogDealColl!=null) {
            objJson.frameObj.dialogDealColl.close();
        }
        objJson.frameObj.location.href = objJson.url;
    }
}
//返回默认首页
function gotoDefaultPortal(){
	if("desktop" == currentPortalTemplate){
		$(".return_ioc2").click();
	}
}
function refreshAllSection(){
	var _mainSrc = $("#main").attr("src");
	if(_mainSrc.indexOf(".psml")>0){
		var _win = $("#main")[0].contentWindow;
		if(_win.sectionHandler){
			_win.sectionHandler.reloadAllSection();
		}
	}
}
//工作桌面进入首页时,进行定位
function refreshSpaceCurrentClass(spaceId){
  $(".nav_center_currentIco").each(function(i,obj){
    if($(obj).attr("id") == ("_currentIco_"+spaceId)){
      $(obj).show();
    }else{
      $(obj).hide();
    }
  });
}
//栏目更多页面现在当前位置方法
function showMoreSectionLocation(text){
  var html = '<span class="nowLocation_ico"><img src="'+_ctxPath+'/main/skin/frame/'+skinPathKey+'/menuIcon/moresectionicon.png"></span>';
      html += '<span class="nowLocation_content">';
      html += '<a style="color:#888;">' + escapeStringToHTML(text,false) + '</a>';
      html += '</span>';
      getCtpTop().showLocation(html);
}
//刷新工作桌面待办列表
function refreshDeskTopPendingList(){
	var _mainSrc = $("#desktopIframe").attr("src");
	if(_mainSrc && _mainSrc.indexOf("deskTopController.do")>0){
		var _win = $("#desktopIframe")[0].contentWindow;
		if(_win.$.initDeskPending){
			_win.$.initDeskPending();
		}
	}
}