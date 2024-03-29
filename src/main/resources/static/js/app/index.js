var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });

        $('#btn-update').on('click', function () {
                    _this.update();
                });

        $('#btn-delete').on('click', function () {
                            _this.delete();
                        });

        $('#btn-local-login').on('click', function () {
                            _this.local_login();
                        });
         $('#btn-change-user-info').on('click', function () {
                                    _this.change_user_info();
                        });
         $('#btn-local-register').on('click', function () {
                             _this.local_register();
                            });

         $('#btn-goto-edit').on('click', function () {
                             _this.goto_edit();
                            });
    },
    save : function () {
        var data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('글이 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            var parsedMessage = error.responseJSON.error + ": " +error.responseJSON.message;
           alert(parsedMessage);
        });
    },
    update : function () {
            var data = {
                title: $('#title').val(),
                content: $('#content').val()
            };

            var id = $('#id').val();

            $.ajax({
                type: 'PUT',
                url: '/api/v1/posts/'+id,
                dataType: 'json',
                contentType:'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function() {
                alert('글이 수정되었습니다.');
                window.location.href = '/';
            }).fail(function (error) {
                var parsedMessage = error.responseJSON.error + ": " +error.responseJSON.message;
               alert(parsedMessage);
            });
    },

    delete : function () {
            var id = $('#id').val();

            $.ajax({
                type: 'DELETE',
                url: '/api/v1/posts/'+id,
                contentType:'application/json; charset=utf-8'
            }).done(function() {
                alert('글이 삭제되었습니다.');
                window.location.href = '/';
            }).fail(function (error) {
                var parsedMessage = error.responseJSON.error + ": " +error.responseJSON.message;
               alert(parsedMessage);
            });
    },

    local_login : function () {
                var data = {
                    email: $('#email').val(),
                    password: $('#password').val()
                };

                $.ajax({
                    type: 'POST',
                    url: '/api/v1/local-login',
                    dataType: 'json',
                    contentType:'application/json; charset=utf-8',
                    data: JSON.stringify(data)
                }).done(function() {
                    alert('로그인 완료');
                    window.location.href = '/';
                }).fail(function (error) {
                   var parsedMessage = error.responseJSON.error + ": " +error.responseJSON.message;
                   alert(parsedMessage);
                });
    },

    change_user_info : function () {
                var data = {
                    name: $('#name').val(),
                    password: $('#password').val()
                };

                $.ajax({
                    type: 'PUT',
                    url: 'api/v1/change-user-info',
                    dataType: 'json',
                    contentType:'application/json; charset=utf-8',
                    data: JSON.stringify(data)
                }).done(function() {
                    alert('수정 완료');
                    window.location.href = '/';
                }).fail(function (error) {
                    var parsedMessage = error.responseJSON.error + ": " +error.responseJSON.message;
                    alert(parsedMessage);
                });
    },

    local_register : function () {
                var data = {
                    name: $('#name').val(),
                    email: $('#email').val(),
                    password: $('#password').val()
                };

                $.ajax({
                    type: 'POST',
                    url: 'api/v1/local-register',
                    dataType: 'json',
                    contentType:'application/json; charset=utf-8',
                    data: JSON.stringify(data)
                }).done(function() {
                    alert('등록 완료');
                    window.location.href = '/';
                }).fail(function (error) {
                    var parsedMessage = error.responseJSON.error + ": " +error.responseJSON.message;
                    alert(parsedMessage);
                });
    },

    goto_edit : function () {
             var data = {
                 id: $('#id').val(),
             };

             $.ajax({
                 type: 'GET',
                 url: '/api/v1/validate-edit?id=' + $('#id').val(),
                 dataType: 'json',
                 contentType:'application/json; charset=utf-8',
             }).done(function(responseJSON) {
                if(responseJSON)
                    window.location.href = '/posts/update/' + data.id;
                else
                    alert("내가 작성한 글이 아닙니다.");
             }).fail(function (error) {
                 var parsedMessage = error.responseJSON.error + ": " +error.responseJSON.message;
                 alert(parsedMessage);
             });
         }
}

main.init();