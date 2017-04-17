/**
 * Main javascript file for running animations on the page
 */
$(document).ready(function() {
    // $('.container').hide().fadeIn('slow');
    
    $('.feature-main-heading')
        .hide()
        .delay(400)
        .fadeIn('slow');
    
    $('.feature-sub-heading')
        .hide()
        .delay(500)
        .fadeIn('slow');
    
    $('#playstore-button')
        .hide()
        .delay(600)
        .fadeIn('slow');
    
    $('#hero')
        .hide()
        .fadeIn('slow');
    
    // inview plugin used from below site
    // https://github.com/protonet/jquery.inview
    
    $('.section-features, .section-contribute, .screenshots').hide();
    
    $('.section-features-and-contribute').one('inview', function(event, isInView) {
        $('.section-features')
            .delay(400)
            .show()
            .addClass('animation-slidein-left');
        $('.section-contribute')
            .delay(500)
            .show()
            .addClass('animation-slidein-right');
    });
    
    $('.section-screenshots').one('inview', function(e, isInView){
        $('.screenshots')
            .delay(500)
            .slideDown(1000);
    });
    
});
