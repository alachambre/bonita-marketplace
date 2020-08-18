
$(document).ready(function () {

    $('#create-artifact-button').click(function () {
        var artifactName = $('#artifact-name').val();
        var artifactDescription = $('#artifact-description').val();
        var groupId = $('#groupId').val();
        var artifactId = $('#artifactId').val();
        var version = $('#version').val();
        $.post({
            url: '/maven-artifact',
            contentType: 'application/json',
            data: JSON.stringify({name: artifactName,
            description: artifactDescription,
            groupId: groupId,
            artifactId: artifactId,
            version: version})
        });
    });
    
    $('#search-artifact-button').click(function () {
        var searchName = $('#searchName').val();
        $.get({
                url: '/maven-artifact/search?name=' + searchName,
                contentType: 'application/json',
                success: function (artifacts) {
                    var list = '';
                    (artifacts || []).forEach(function (artifact) {
                        list = list
                            + '<tr>'
                            + '<td>' + artifact.id + '</td>'
                            + '<td>' + artifact.name + '</td>'
                            + '<td>' + artifact.description + '</td>'
                            + '<td>' + artifact.groupId + '</td>'
                            + '<td>' + artifact.artifactId + '</td>'
                            + '<td>' + artifact.version + '</td>'
                            + '</tr>'
                    });
                    if (list.length > 0) {
                        list = ''
                            + '<table><thead><th>Id</th><th>Name</th><th>Description</th><th>groupId</th><th>artifactId</th><th>version</th><th></th></thead>'
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