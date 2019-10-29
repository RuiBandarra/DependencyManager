$(document).ready(function() {
    //Prevent default post for the form on submit
    $("#form").submit(function (e) {
        e.preventDefault();
    });

    $('#form-submit').click(function () {
        jsRoutes.controllers.DependencyManagerController.handleFormSubmit().ajax({
            data: $("#form").serialize(),
            success: function (data) {
                $('#results').html(data)
            },
            error: function (err) {
                $("#loginAlert").show()
                $('#results').html(err)
            }
        });
    });
});