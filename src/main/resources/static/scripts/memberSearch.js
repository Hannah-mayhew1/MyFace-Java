function searchMembers() {
    var input, filter, users, p, i, txtValue;
    input = document.getElementById("search-members");
    filter = input.value.toUpperCase();
    users = document.getElementById("users");
    p = users.getElementsByTagName("p");

    for (i = 0; i < p.length; i++) {
        txtValue = p[i].textContent;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            p[i].style.display = "";
        } else {
            p[i].style.display = "none";
        }
    }
}