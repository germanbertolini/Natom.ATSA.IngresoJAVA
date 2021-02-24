atsaServices.factory('AfiliadosService', ['$resource', '$http', function($resource, $http){
	return {
		findTramitesPageByTipo: function(tipo, id, page) {
			var url = baseUrl + 'afiliadosservice/findTramitesPageByTipo';
            return $resource(url, {'tipo': tipo, 'id': id, 'page': page});
	    },
	    
		findByCUIL: function(cuil) {
			var url = baseUrl + 'afiliadosservice/findByCUIL';
            return $resource(url, {'cuil': cuil});
	    },
	    
	    findByDocumento: function(documento) {
			var url = baseUrl + 'afiliadosservice/findByDocumento';
            return $resource(url, {'documento': documento});
	    },
	    
	    findEstablecimientos: function(nombre, cuit) {
	    	var url = baseUrl + 'establecimientosservice/findByNombreAndCuit';
            return $resource(url, {'nombre': nombre, 'cuit': cuit});
	    },
	    
	    findByNumeroAfiliado: function(numeroAfiliado) {
			var url = baseUrl + 'afiliadosservice/findByNumeroAfiliado';
            return $resource(url, {'numeroAfiliado': numeroAfiliado});
	    },
	
		getAfiliado: function(afiliadoId) {
			var url = baseUrl + 'afiliadosservice/getAfiliado';
            return $resource(url, {'afiliadoId': afiliadoId});
	    },
        
	    saveAfiliado: function() {
            var url = baseUrl + 'afiliadosservice/saveAfiliado';
            return $resource(url, {}, {
            	saveAfiliado: {
                    method: 'PUT'
                }
            });
        },
        
        reafiliarAfiliado: function() {
            var url = baseUrl + 'afiliadosservice/reafiliarAfiliado';
            return $resource(url, {}, {
            	reafiliarAfiliado: {
                    method: 'PUT'
                }
            });
        },
        
        desafiliarAfiliado: function() {
            var url = baseUrl + 'afiliadosservice/desafiliarAfiliado';
            return $resource(url, {}, {
            	desafiliarAfiliado: {
                    method: 'PUT'
                }
            });
        },
        
        pasivarAfiliado: function() {
            var url = baseUrl + 'afiliadosservice/pasivarAfiliado';
            return $resource(url, {}, {
            	pasivarAfiliado: {
                    method: 'PUT'
                }
            });
        },
        
        darPorAfiliado: function() {
            var url = baseUrl + 'afiliadosservice/darPorAfiliado';
            return $resource(url, {}, {
            	darPorAfiliado: {
                    method: 'PUT'
                }
            });
        },
        
        aprobarTramite: function() {
            var url = baseUrl + 'afiliadosservice/aprobarTramite';
            return $resource(url, {}, {
            	aprobarTramite: {
                    method: 'PUT'
                }
            });
        },
        
        rechazarTramite: function() {
            var url = baseUrl + 'afiliadosservice/rechazarTramite';
            return $resource(url, {}, {
            	rechazarTramite: {
                    method: 'PUT'
                }
            });
        },
	    
        findTramitesAprobados: function() {
			var url = baseUrl + 'afiliadosservice/findTramitesAprobados';
            return $resource(url, {});
	    },
	    
        findTramitesAprobadosTramites: function() {
			var url = baseUrl + 'afiliadosservice/findTramitesAprobadosTramites';
            return $resource(url, {});
	    },
	    
        findTramitesAprobadosDocumentos: function() {
			var url = baseUrl + 'afiliadosservice/findTramitesAprobadosDocumentos';
            return $resource(url, {});
	    },
	    
	    findTramitesRechazados: function() {
			var url = baseUrl + 'afiliadosservice/findTramitesRechazados';
            return $resource(url, {});
	    },
	    
	    findTramitesRechazadosTramites: function() {
			var url = baseUrl + 'afiliadosservice/findTramitesRechazadosTramites';
            return $resource(url, {});
	    },
	    
	    findTramitesRechazadosDocumentos: function() {
			var url = baseUrl + 'afiliadosservice/findTramitesRechazadosDocumentos';
            return $resource(url, {});
	    },
	    
	    findTramitesPorAprobar: function() {
			var url = baseUrl + 'afiliadosservice/findTramitesPorAprobar';
            return $resource(url, {});
	    },
	    
	    findTramitesPorAprobarDocumentos: function() {
			var url = baseUrl + 'afiliadosservice/findTramitesPorAprobarDocumentos';
            return $resource(url, {});
	    },
	    
	    findFamiliarDocumentos: function(id) {
			var url = baseUrl + 'afiliadosservice/findFamiliarDocumentos';
            return $resource(url, {'id': id});
	    },
	    
	    findTramites: function(id) {
			var url = baseUrl + 'afiliadosservice/findTramites';
            return $resource(url, {'id': id});
	    },
	    
	    findFamiliarByDocumento: function(documento) {
			var url = baseUrl + 'afiliadosservice/findFamiliarByDocumento';
            return $resource(url, {'documento': documento});
	    },
	    
	    findLocalidades: function() {
	    	var url = baseUrl + 'afiliadosservice/findLocalidades';
            return $resource(url, {});
	    },
	    
	    findLocalidadByNombre: function(nombre) {
	    	var url = baseUrl + 'afiliadosservice/findLocalidadByNombre';
            return $resource(url, {'nombre': nombre});
	    },
	    
	    generarCarnetSilent: function(id) {
	    	var url = baseUrl + 'carnetservice/generarCarnetSilent';
            return $resource(url, {'id': id});
	    },
        findAfiliadoByFamiliarId: function(familiarId) {
            var url = baseUrl + 'afiliadosservice/findAfiliadoByFamiliarId';
            return $resource(url, {'familiarId': familiarId});
        }
	}
}]);