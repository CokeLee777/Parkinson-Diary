$("#chkAll").click(function() {
    if ($('#chkAll').prop('checked')) {
        $("input[name=unsignedUser]").attr("checked", "checked");
        $("input[name=unsignedUser]").prop("checked", true);
    } else {
        $("input[name=unsignedUser]").removeAttr("checked", "");
        $("input[name=unsignedUser]").prop("checked", false);
    }
    $("input[name=unsignedUser]").click(function() {
        if ($("input[name=unsignedUser]").length == $("input[name=unsignedUser]:checked").length) {
            $("#chkAll").attr("checked", "checked");
            $("#chkAll").prop("checked", true);
        } else {
            $("#chkAll").removeAttr("checked", "");
            $("#chkAll").prop("checked", false);
        }
    });
});

$("#chkAll2").click(function() {
    if ($('#chkAll2').prop('checked')) {
        $("input[name=signedUser]").attr("checked", "checked");
        $("input[name=signedUser]").prop("checked", true);
    } else {
        $("input[name=signedUser]").removeAttr("checked", "");
        $("input[name=signedUser]").prop("checked", false);
    }
    $("input[name=signedUser]").click(function() {
        if ($("input[name=signedUser]").length == $("input[name=signedUser]:checked").length) {
            $("#chkAll2").attr("checked", "checked");
            $("#chkAll2").prop("checked", true);
        } else {
            $("#chkAll2").removeAttr("checked", "");
            $("#chkAll2").prop("checked", false);
        }
    });
});