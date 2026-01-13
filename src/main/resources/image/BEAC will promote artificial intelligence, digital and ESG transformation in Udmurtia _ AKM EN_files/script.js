$(document).ready(function () {
    $('#register_button').click(function(){
        console.log('include/register_form.php');
        if(window.location.pathname.includes('/eng/')){
            $.ajax({
                type: "POST",
                url: '/eng/include/paid_access/register_form.php',
                success: function(data) {
                    $('#register_form').html(data);
                }
            });
        } else {
            $.ajax({
                type: "POST",
                url: '/include/paid_access/register_form.php',
                success: function(data) {
                    $('#register_form').html(data);
                }
            });
        }

    });
})