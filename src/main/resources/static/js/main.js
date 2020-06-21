function showMask()
{
    var mask = $('#mask');
    mask.css({'width': $(document).width(), 'height': $(document).height()})
        .fadeIn(400).fadeTo("slow", 0.90);
}


function showModalWindow(id)
{
    showMask();
    var w = $(id).width();
    var h = $(id).height();
    $(id).css('left', window.innerWidth/2  - w/2 );
    $(id).css('top',  window.innerHeight/2 - h/2 );
    $(id).fadeIn(400);
}


function loadDataToModalWindow(id, url, time){

    if( $('div'+id+' div.content').html() === '') {
        $('#loader').show();
        $.ajax({
            url: url,
            cache: false,
            dataType: "html",
            timeout: time,
            success: function(data){
                $(data).appendTo('div'+id+' div.content');
                $('.window').hide();
                $('#mask').hide();
                showModalWindow(id);
            },
            error: function(data){
                $('.window').hide();
                $('#mask').hide();
                //setTimeout(function(){ alert('Не удалось получить данные. Попробуйте ещё.'); }, 100);
                doErrorMessageNoRest(data);
            },
            complete: function () {
                $('#loader').hide();
            }
        });

    }
    else{
        $('.window').hide();
        showModalWindow(id);
    }

}


function loadEntityByIdInModalWindowRest(id, url, time, eid, modalWindowTitle, entityType){

    $(id).find('div.name').html('' + modalWindowTitle + '');  // В любом случае устанавливаем заголовок окна

    //if( $('div'+id+' div.content').html() === '' || mapEntity.get('Entity' + eid) == null ) {
    if( true ) {
        $('#loader').show();
        $.ajax({
            url: url,
            cache: false,
            type: "GET",
            timeout: time,
            success: function(data){
                $('.window').hide();
                $('#mask').hide();
                // Добавляем полученный объект в карту
                mapEntity.set('Entity' + eid, data);
                FormFormirate(id, mapEntity.get('Entity' + eid), eid, entityType);
                showModalWindow(id);
            },
            error: function(data){
                $('.window').hide();
                $('#mask').hide();
                //setTimeout(function(){ alert('Не удалось получить данные. Попробуйте ещё.'); }, 100);
                doErrorMessageNoRest(data);
            },
            complete: function () {
                $('#loader').hide();
            }
        });

    }
    else{
        $('.window').hide();
        FormFormirate(id, mapEntity.get('Entity' + eid), eid, entityType);
        showModalWindow(id);
    }

}


function doErrorMessageNoRest(data){
    $('#loader').hide();
    if( typeof data.status == "undefined" || data.status == null || data.status == 0 ){
        ShowStatusMessage("<span>Таймаут запроса превышен!</span>", "error");
    }
    else{
        ShowStatusMessage('Ошибка. Статус: ' + data.status, "error");
    }
}


function loadRestDataGet(url, time){
    loadRestDataBase(null, url, time, 'GET');
}


function loadRestDataPost(id, url, time){
    loadRestDataBase(id, url, time, 'POST');
}


function loadRestDataPut(id, url, time){
    loadRestDataBase(id, url, time, 'PUT');
}


function loadRestDataDelete(url, time){
    if ( confirm('Вы уверены? Данные будут безвозвратно удалены.') ) {
        loadRestDataBase(null, url, time, 'DELETE');
    }
}


function loadRestDataBase(id, url, time, type){
    $('#loader').show();
    $.ajax({
        url: url,
        cache: false,
        type: type,
        timeout: time,
        data: $(id).serialize(),
        success: function(data){
            $('#loader').hide();
            ShowStatusMessageWithReload(data.message, data.status);
        },
        error: function(data){
            doErrorMessageRest(data);
        }
    });
}


function doErrorMessageRest(data){
    $('#loader').hide();
    ShowStatusMessage(getErrorMessageFromData(data), "error");
}


function updateDataRest(){

    $('#loader').show();

    $.ajax({
        url: '/rest' + location.pathname + location.search,
        cache: false,
        type: "GET",
        timeout: 2000,
        success: function(data) {
            if( typeof curEntity != "undefined" && curEntity != null ){
                mapEntity.clear();
                dataParseRest(data);  // Обновление данных, которые пришли по Rest
            }
        },
        error: function() {
            //setTimeout(function(){ alert('Не удалось получить данные!'); }, 100);
            setTimeout(function(){ location.reload(); }, 100);
        },
        complete: function() {
            isReload = 0;
            $('#loader').hide();
            $('.window').hide();
            $('#mask').hide();
        }
    });
}


var isReload = 0;   // Флаг того, что надо обновить данные по текущему url по REST, или перезагрузить страницу если данные не получены


var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(header, token);
});


function isInt(value){
    var n = value != null ?  parseInt(value) : 0;
    return isNaN(n) ? 0 : value;
}


function isFloat(value){
    var n = value != null ?  parseFloat(value) : 0;
    return isNaN(n) ? 0 : value;
}


function isData(value){
    var n = value != null ?  parseDate(value) : 0;
    return isNaN(n) ? 0 : value;
}


function formToJSON(f){
    // event.target — это HTML-элемент form
    let formData = new FormData( f );
    // Собираем данные формы в объект
    let obj = {};
    formData.forEach(
        (value, key) => obj[key] = key == 'user' || key == 'restaurant' ||
        key == 'meal' || key == 'vote' ? {"id": isInt(value)} : value
    );
    if( obj["mark"] != null ) obj["mark"] = isInt(obj["mark"]);
    if( obj["price"] != null ) obj["price"] = isFloat(obj["price"]);
    if( obj["date"] != null ) obj["date"] = isData(obj["date"]);

    return obj;
}


function SendAjaxFormRest(id, url, time, method, reloadExternalSources) {

    //let obj = formToJSON( $(id).find('form.AjaxForm')[0] );  // For JSON from form

    $('#loader').show();

    $.ajax({
        url: url,
        cache: false,
        type: method,
        timeout: time,
        data: $(id).find('form.AjaxForm').serialize(),
        /*        data: JSON.stringify(obj),
                datatype: 'json',
                contentType: "application/json",*/
        success: function(data) {
            doGoodInModalWindow(id, data, reloadExternalSources)
        },
        error: function(data) {
            doErrorInModalWindow(id, data, reloadExternalSources)
        },
        complete: function(data) {
            showModalWindow( '#' + $(id).parents('div.window').attr('id') );
        }
    });
}


function SendAjaxFormRestId(id, url, time, method, reloadExternalSources) {
    $('#loader').show();
    $.ajax({
        url: url,
        cache: false,
        type: method,
        timeout: time,
        data: $(id).find('form.AjaxForm').serialize(),
        success: function(data) {
            doGoodInModalWindow(id, data, reloadExternalSources)
        },
        error: function(data) {
            doErrorInModalWindow(id, data, reloadExternalSources)
        },
        complete: function(data) {
            showModalWindow( '#' + $(id).parents('div.window').attr('id') );
        }
    });
}


function doGoodInModalWindow(id, data, reloadExternalSources){
    $('#loader').hide();
    $('.window').hide();
    // Обработка ответа сервера в форму
    if( reloadExternalSources == true ){
        grecaptcha.reset();  // Обновляем капчу
    }
    var mes = $(id).find('#message');
    mes.removeClass();
    mes.html('');
    mes.addClass(data.status + '');
    mes.html(data.message + '');
    $(id).find('div.invalid-feedback').html('');
    for (var i in data.params) {
        $(id).find('div#' + i + '').html(''+ data.params[i] + '');
    }
    if( data.status == "good" ){
        isReload = 1;
    }
    else{
        isReload = 0;
    }
}


function doErrorInModalWindow(id, data, reloadExternalSources){
    isReload = 0;
    $('#loader').hide();
    $('.window').hide();
    if( reloadExternalSources == true ){
        grecaptcha.reset();  // Обновляем капчу
    }
    var mes = $(id).find('#message');
    mes.removeClass();
    mes.html('');
    mes.addClass('error');
    mes.html( getErrorMessageFromData(data) );
    $(id).find('div.invalid-feedback').html('');
    if( data.responseJSON.params != "undefined" && data.responseJSON.params != null  ){
        for (var i in data.responseJSON.params) {
            $(id).find('div#' + i + '').html(''+ data.responseJSON.params[i] + '');
        }
    }
}


function getErrorMessageFromData(data){
    if( ( typeof data.responseJSON == "undefined" || data.responseJSON == null ) &&
        ( typeof data.responseText == "undefined" || data.responseText == null ) ) {
        return '<span>Таймаут запроса превышен!</span>';
    }
    else{
        if( typeof data.responseJSON != "undefined" && data.responseJSON != null ){
            if( data.responseJSON.status == 'error' ){
                return data.responseJSON.message + '';
            }
            else{
                return 'Ошибка. Статус: ' + data.responseJSON.status
            }
        }
        else{
            return 'Ошибка. Статус: ' + data.status;
        }
    }
}


function ShowStatusMessage(message, status){
    var content = '<div class="' + status + '">' + message + '</div>';
    var mes = $('div#NewMessage');
    mes.find('div.content').html('');
    $(content).appendTo(mes.find('div.content'));
    showModalWindow('div#NewMessage');
}


function ShowStatusMessageWithReload(message, status){
    ShowStatusMessage(message, status);
    if( status == "good" ){ isReload = 1; }
}


$(document).ready(function(){

    $('#mask').click(function(){
        clearInputsInModalWindow();
        if( isReload == 1 ){ updateDataRest(); }
        else{ $(this).hide();$('.window').hide(); }
    });

    var loader = $('#loader');
    loader.css('left',  window.innerWidth/2   - loader.width()/2  )
        .css('top',   window.innerHeight/2  - loader.height()/2 );


    $('.modal_window .close').click(function(){
        clearInputsInModalWindow();
        if( isReload == 1 ){ updateDataRest(); }
        else{ $('#mask').hide();$('.window').hide(); }
    });

    // Подсветка меню
    var location = window.location.href;
    $('a.navItem').each(function () {
        var link = $(this).attr('href');
        if ( location.indexOf(link) > -1  ) { $(this).addClass('currentNavItem'); }
    });

    // For i18n
    $('select#langSelect').change(function(){
        window.location.href = '/' + $('select#langSelect option:selected').val();
    });

});


$(document).keydown(function(eventObject){
    if( eventObject.which === 27 ){
        clearInputsInModalWindow();
        if( isReload == 1 ){ updateDataRest(); }
        else{ $('#mask').hide();$('.window').hide(); }
    }
});


function AddUserComment(meadId){
    $('div#AllFormCommentAddUser').find('#mealId').val(meadId);
    showModalWindow('#AddComment')
}


function UpdateUserComment(meadId, text, eid){
    var div = $('div#AllFormCommentUpdateUser');
    div.find('#message').html('');
    div.find('.invalid-feedback').html('');
    div.find('#Cid').val(eid);
    div.find('#eid').val(eid);
    div.find('#comment').val(text);
    div.find('#mealId').val(meadId);
    showModalWindow('#UpdateComment')
}


function clearInputsInModalWindow() {
    var mw = $('.modal_window');
    mw.find('div#message').html('');
    mw.find('.invalid-feedback').html('');
    mw.find('textarea').val('');
}


function deleteUser(id, time, tr){

    $('div#message').html('');

    $.ajax({
        url: '/rest/users/' + id,
        cache: false,
        type: 'DELETE',
        timeout: time,
        success: function(data){
            var div = $('div#message');
            showMessage(div);
            div.html(data.message);
            div.addClass(data.status);
            tr.detach();
            reFreshUserData();
        },
        error: function(data){
            var div = $('div#message');
            showMessage(div);
            div.html('Ошибка удаления пользователя!');
            div.addClass('error');
        },
        complete: function(){
            hideMessage();
        }
    });

}



function showMessage(div) {
    div.stop(true);
    div.css("opacity", 1.0);
    div.removeClass();
    div.show();
}


function hideMessage() {
    var div = $('div#message');
    div.animate({
        opacity: 0.2,
    }, 2500, function() {
        div.hide(800);
        div.html('');
    });
}


function reFreshUserData(){
    var trArr = $('tr.userTr');
    $('b#countUsers').text( '' + trArr.length + '' );
    var i = 0;
    trArr.each(function(){
        i++;
        $(this).find('td.numberTd').text('' + i + '');
    });
}
