'use strict';

atsaServices.factory('HomeService', ['$resource', '$http', function($resource, $http){
    return {
    	getAllEstablecimientos: function() {
            var url = baseUrl + 'parametrosservice/getAllEstablecimientos';
            return $resource(url, {});
        },
        getAllTiposBaja: function() {
            var url = baseUrl + 'parametrosservice/getAllTiposBaja';
            return $resource(url, {});
        },
        getAllEstados: function() {
            var url = baseUrl + 'parametrosservice/getAllEstados';
            return $resource(url, {});
        },
        getAllPaises: function() {
            var url = baseUrl + 'parametrosservice/getAllPaises';
            return $resource(url, {});
        },
        getAllRoles: function() {
            var url = baseUrl + 'parametrosservice/getAllRoles';
            return $resource(url, {});
        },
        getAllTipoDocumentosArchivados: function() {
            var url = baseUrl + 'parametrosservice/getAllTipoDocumentosArchivados';
            return $resource(url, {});
        },
        getAllTipoRelaciones: function() {
            var url = baseUrl + 'parametrosservice/getAllTipoRelaciones';
            return $resource(url, {});
        },
        getAllTipoTramites: function() {
            var url = baseUrl + 'parametrosservice/getAllTipoTramites';
            return $resource(url, {});
        },
        getAllConvenios: function() {
            var url = baseUrl + 'parametrosservice/getAllConvenios';
            return $resource(url, {});
        },
        findAfiliadoByFamiliarId: function(familiarId) {
            var url = baseUrl + 'afiliadosservice/findAfiliadoByFamiliarId';
            return $resource(url, {'familiarId': familiarId});
        }
    }
}]);