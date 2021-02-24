atsaServices.factory('EstablecimientosService', ['$resource', '$http', function($resource, $http){
	return {
		findById: function(id) {
			var url = baseUrl + 'establecimientosservice/findById';
            return $resource(url, {'id': id});
	    },
	    
	    findAll: function(page, nombre, cuil) {
			var url = baseUrl + 'establecimientosservice/findAll';
            return $resource(url, {'page': page, 'nombre': nombre, 'cuil': cuil});
	    },
	    
	    saveEstablecimiento: function() {
            var url = baseUrl + 'establecimientosservice/saveEstablecimiento';
            return $resource(url, {}, {
            	saveEstablecimiento: {
                    method: 'PUT'
                }
            });
        },
        
        eliminarEstablecimiento: function(id) {
			var url = baseUrl + 'establecimientosservice/eliminarEstablecimiento';
            return $resource(url, {'id': id});
	    }
	}
}]);