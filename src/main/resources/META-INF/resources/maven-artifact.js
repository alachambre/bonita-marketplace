
$(document).ready(function () {

    $('#create-artifact-button').click(function () {
        var artifactName = $('#artifact-name').val();
        var artifactDescription = $('#artifact-description').val();
        var artifactType = $('#artifact-type').val();
        var groupId = $('#groupId').val();
        var artifactId = $('#artifactId').val();
        var version = $('#version').val();
        $.post({
            url: '/maven-artifact',
            contentType: 'application/json',
            data: JSON.stringify({name: artifactName,
            description: artifactDescription,
            type: artifactType,
            groupId: groupId,
            artifactId: artifactId,
            version: version})
        });
    });
    
    $('#search-artifact-button').click(function () {
        var searchContent = $('#searchContent').val();
        var searchType = $('#searchType').val();
        $.get({
                url: '/maven-artifact/search/' + searchType + '?searchContent=' + searchContent,
                contentType: 'application/json',
                success: function (artifacts) {
                    var list = '';
                    (artifacts || []).forEach(function (artifact) {
                        list = list
                            + '<tr>'
                            + '<td>' + artifact.name + '</td>'
                            + '<td>' + artifact.description + '</td>'
                            + '<td>' + artifact.type + '</td>'
                            + '<td>' + artifact.groupId + '</td>'
                            + '<td>' + artifact.artifactId + '</td>'
                            + '<td>' + artifact.version + '</td>'
                            + '</tr>'
                    });
                    if (list.length > 0) {
                        list = ''
                            + '<table class="table">'
                            + '<thead>'
                            + '<tr>'
                            + '<th scope="col">Name</th>'
                            + '<th scope="col">Description</th>'
                            + '<th scope="col">Type</th>'
                            + '<th scope="col">groupId</th>' 
                            + '<th scope="col">artifactId</th>'
                            + '<th scope="col">version</th>'
                            + '</tr>'
                            + '</thead>'
                            + list
                            + '</table>';
                    } else {
                        list = "Not found"
                    }
                    $('#search-result').html(list);
                }
        });
    });

});