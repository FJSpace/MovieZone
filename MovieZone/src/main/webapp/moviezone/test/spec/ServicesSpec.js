/*
 *  This is very primitive but hopefully simple to use 
 *  during workshops and project
 *  
 *   If serious about this you should look at Karma (using
 *   Karma and more...). See Web.
 *   
 *   Both the restful_backend and the restful_frontend must run.  
 * 
 */

// This defines the test suite
describe("Test suite for PersonRegService", function() {

    // Could also be in an beforeEach function
    var injector = angular.injector(['PersonRegService', 'ng']);
    var service = injector.get('PersonRegProxy');

    // A gouping of tests
    describe("Grouping all read function", function() {

        var val;
        
        // Run before each test.
        // The done paramters is always (implicit) supplied
        // Now we need it so write it out
        beforeEach(function(done) {
             service.count().success(function(count) {
                val = count.value;
                console.log(val);
                done(); // Signal to Jasmine that the asynchronous call is finished
            });
        });
        
        // This is the test. Call is finished (because of above), 
        // so safe to check value.
        it("Test count", function() {
            expect(val).toBe(101);
        });
    });


    describe("Grouping all write function", function() {
        // Todo 
    });
});