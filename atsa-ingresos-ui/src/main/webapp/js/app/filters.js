'use strict';

/* Filters */

angular.module('myApp.filters', []).
  filter('interpolate', ['version', function(version) {
    return function(text) {
      return String(text).replace(/\%VERSION\%/mg, version);
    }
  }]);

angular.module('informesFilters', [])
    .filter('informesFilter', function() {
        return function(informes, textToSearch, tipoInforme, desde, hasta) {
            console.log("inside the filter. Informes es: " + informes + " tipo de informe es: " + tipoInforme);
            var arrayToReturn = [];
            for (var i=0; i<informes.length; i++){
                if ( textToSearch == null | (informes[i].nombreMateria != null && informes[i].nombreMateria.indexOf(textToSearch) > -1)) {
                    if ( tipoInforme == null |tipoInforme == "" || informes[i].tipoInforme == tipoInforme) {
                        if ( desde == null |desde == "" || informes[i].fecha >= desde) {
                            if ( hasta == null | hasta == "" || informes[i].fecha <= hasta) {

                                arrayToReturn.push(informes[i]);
                            }
                        }
                    }
                }
            }
            return arrayToReturn;
        };
    })
    .filter('dateFilter', function($filter){
        return function(dateNum, format) {
        	if (dateNum == undefined)
        		return;
            var angularDateFilter = $filter('date');
            var dateParts = dateNum.split("/");
            var dt = new Date(dateParts[2], dateParts[1]-1, dateParts[0]);
            //return (dt.getDay() + '/' + dt.getMonth() + '/' + dt.getYear());
            return angularDateFilter(dt, format);
        };
    })
    .filter('alumnosFilter', function() {
        return function(alumnos, textToSearch, tipoDoc, numeroDoc, turno, curso) {
        	if (alumnos) {
	            var arrayToReturn = [];
	            for (var i=0; i < alumnos.length; i++){
	            	var push = true;
	                if (push && textToSearch != null) {
	                	push = false;
	                	if (alumnos[i].nombreCompleto && alumnos[i].nombreCompleto.toLowerCase().indexOf(textToSearch.toLowerCase()) > -1) {
	                		push = true;
	                	}
	                }
	                
	                if (push && tipoDoc !== null) {
	                	push = false;
	                	if (alumnos[i].documentoTipo == tipoDoc) {
	                		push = true;
	                	}
	                }
	                
	                if (push && numeroDoc !== null) {
	                	push = false;
	                	if (alumnos[i].documentoNumero && alumnos[i].documentoNumero.indexOf(numeroDoc) > -1) {
	                		push = true;
	                	}
	                }
	                
	                if (push && turno !== null) {
	                	push = false;
	                	if (alumnos[i].cursos) {
	                		for (var j = 0; j < alumnos[i].cursos.length; j++) {
	                			if (alumnos[i].cursos[j] && alumnos[i].cursos[j].turno == turno) {
	                				push = true;
	                			}
	                		}
	                	}
	                }
	                
	                if (push && curso !== null) {
	                	push = false;
	                	if (alumnos[i].cursos) {
	                		for (var j = 0; j < alumnos[i].cursos.length; j++) {
	                			if (alumnos[i].cursos[j] && alumnos[i].cursos[j].id == curso) {
	                				push = true;
	                			}
	                		}
	                	}
	                }
	                
	                if (push) {
	                	arrayToReturn.push(alumnos[i]);
	                }
	            }
        	}
            return arrayToReturn;
        };
    })
;

//angular.module('datesFilters', [])
