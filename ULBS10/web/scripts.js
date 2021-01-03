$(document).ready(function() {
    function toggleSection(section) {
        $(".content-section").removeClass("active");
        $(".content-section[data-tab='" + section + "']").addClass("active");
        
        if(section == "adauga-post" || section == "edit-post") {
            $(".sidebar-links a div").removeClass("active");
            $(".sidebar-links a[data-location='posturi'] div").addClass("active");
        } else if(section == "adauga-user" || section == "edit-user") {
            $(".sidebar-links a div").removeClass("active");
            $(".sidebar-links a[data-location='utilizatori'] div").addClass("active");
        } else {
            $(".sidebar-links a div").removeClass("active");
            $(".sidebar-links a[data-location='" + section + "'] div").addClass("active");
        }
    }

    //
    var currentURL = window.location.href;
    var currentTab = currentURL.split("#")[1];
    if(currentTab) {
        toggleSection(currentTab);
    }

    //
    $(document).on("click", ".sidebar-links a div", function() {
        var href = $(this).parent().attr("href");
        var tab = href.substr(1, href.length);
        tab = tab.split("#")[1];
        toggleSection(tab);
    });

    //
    $(".add-post-link").on("click", function() {
        toggleSection("adauga-post");
    });
    $(".add-user-link").on("click", function() {
        toggleSection("adauga-user");
    });

    //
    $(".edit-post-link").on("click", function() {
        var postID = $(this).data("post-id"),
            postDenumire = $(this).data("post-denumire"),
            postCerinteMinime = $(this).data("post-cerinte-minime"),
            postCerinteOptionale = $(this).data("post-cerinte-optionale"),
            postDataLimita = $(this).data("post-data-limita");
            
            postCerinteMinime = postCerinteMinime.replaceAll("<br />", "");
            postCerinteOptionale = postCerinteOptionale.replaceAll("<br />", "");

        $(".edit-post-form").prop("action", "./post?action=editpost&id=" + postID);
        $(".edit-post-form-denumire").val(postDenumire);
        $(".edit-post-form-cerinte-minime").val(postCerinteMinime);
        $(".edit-post-form-cerinte-optionale").val(postCerinteOptionale);
        $(".edit-post-form-data-limita").val(postDataLimita);

        toggleSection("edit-post");
    });
    
    $(".edit-user-link").on("click", function() {
        var userID = $(this).data("user-id"),
            userFirstName = $(this).data("user-first-name"),
            userLastName = $(this).data("user-last-name"),
            userEmail = $(this).data("user-email"),
            userStatut = $(this).data("user-statut");

        $(".edit-user-form").prop("action", "./login/user?action=edituser&id=" + userID);
        $(".edit-user-form-first-name").val(userFirstName);
        $(".edit-user-form-last-name").val(userLastName);
        $(".edit-user-form-email").val(userEmail);
        $(".edit-user-form-statut option[value='" + userStatut + "']").attr("selected", "selected");

        toggleSection("edit-user");
    });
});