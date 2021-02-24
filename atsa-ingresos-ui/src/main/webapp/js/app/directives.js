'use strict';

/* Directives */
var atsaDirectives = angular.module('atsaDirectives', []).
  directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
      elm.text(version);
    };
  }])

atsaDirectives.directive('ngConfirmClick', ['$modal', '$rootScope',
    function($modal,$rootScope) {

      var ModalInstanceCtrl = function($scope, $modalInstance) {
	      $scope.ok = function() {
	        $modalInstance.close();
	      };
	
	      $scope.cancel = function() {
	        $modalInstance.dismiss('cancel');
	      };
      };

    return {
      restrict: 'A',
      scope: {
    	  confirmedClick:"&"
      },
      link: function(scope, element, attrs) {
        element.bind('click', function() {
        	$rootScope.showConfirm(scope, attrs.ngConfirmClick);	
        /*	
        	
          var message = attrs.ngConfirmClick || "Are you sure ?";

          var modalHtml = '<div class="modal-header"><div class="header-title">Error</div></div>';
		  modalHtml +='<div class="modal-body"><div class="error-message">' + message + '</div></div>';
		  modalHtml += '<div class="modal-footer"><button class="btn btn-primary" ng-click="ok()">OK</button><button class="btn btn-primary" ng-click="cancel()">Cancel</button></div>';

          var modalInstance = $modal.open({
        	backdrop : 'static',
        	keyboard: false,
            template: modalHtml,
            controller: ModalInstanceCtrl,
            windowClass: 'confirm-modal'
          });
          
          modalInstance.result.then(function() {
            scope.confirmedClick();
          }, function() {
            //Modal dismissed
          });
          */
        });
      }
    }
  }
]);

atsaDirectives.directive('a', function() {
    return {
        restrict: 'E',
        link: function(scope, elem, attrs) {
            if(attrs.ngClick || attrs.href === '' || attrs.href === '#'){
                elem.on('click', function(e){
                    e.preventDefault();
                });
            }
        }
   };
});

atsaDirectives.directive('ngADisabled', function() {
    return {
        compile: function(tElement, tAttrs, transclude) {
            //Disable href, based on class
            tElement.on("click", function(e) {
                if (tElement.hasClass("disabled")) {
                    e.preventDefault();
                }
            });

            //Disable ngClick
            tAttrs["ngClick"] = ("ng-click", "!("+tAttrs["ngADisabled"]+") && ("+tAttrs["ngClick"]+")");

            //Toggle "disabled" to class when aDisabled becomes true
            return function (scope, iElement, iAttrs) {
                scope.$watch(iAttrs["ngADisabled"], function(newValue) {
                    if (newValue !== undefined) {
                        iElement.toggleClass("disabled", newValue);
                    }
                });
            };
        }
    };
});

atsaDirectives.directive('dropzone', function() {
	return function(scope, element, attrs) {
		var config, dropzone;

		config = scope[attrs.dropzone];

		// create a Dropzone for the element with the given options
		dropzone = new Dropzone(element[0], config.options);

		// bind the given event handlers
		angular.forEach(config.eventHandlers, function(handler, event) {
			dropzone.on(event, handler);
		});
		dropzone.on('addedfile', function(file) {
            scope.$apply(function(){
                //console.log(file);
                scope.files.push({name:file.name, file:file});
              });
            });
		dropzone.on('removedfile', function(file){
			scope.$apply(function(){
				scope.files.forEach(function(e){
					scope.files = scope.files.filter(function(e){return e.name != file.name;});
				});
			});
		});
	};
});