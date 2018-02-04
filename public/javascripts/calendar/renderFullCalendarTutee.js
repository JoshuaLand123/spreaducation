$(document).ready(function() {

    $('#calendar').fullCalendar({
        height: 'auto',
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'agendaWeek,agendaDay,listMonth'
        },
        defaultView: 'agendaWeek',
        nowIndicator: true,
        locale: 'de',
        navLinks: true, // can click day/week names to navigate views
        eventDurationEditable: false,
        eventLimit: true,
        allDaySlot: false,
        slotDuration: '00:30:00',
        slotLabelFormat: 'H:mm',
        slotLabelInterval: '01:00:00',
        forceEventDuration: true,
        minTime: '08:00:00',
        maxTime: '22:00:00',
        defaultTimedEventDuration: '01:30:00',
        eventRender: function(event, element) {
            if (event.title == 'Requested') {
                element.find(".fc-bg").css("pointer-events", "none");
                element.append("<div style='position:absolute;bottom:0px;right:0px' ><button type='button' id='btnDeleteEvent' class='btn btn-sm btn-block btn-primary btn-flat'>X</button></div>");
                element.find("#btnDeleteEvent").click(function() {
                    if (confirm("Bist du sicher, dass du dieses Ereignis löschen willst?")) {
                        $.ajax({
                            url: '/events/' + event.id + '/delete',
                            success: function(response) {
                                $('#calendar').fullCalendar('removeEvents', event._id);
                            },
                            error: function(e) {
                                alert(e);
                            }
                        });
                    }

                });
            }
        },
        eventClick: function(event, jsEvent) {
           if (document.elementFromPoint(jsEvent.pageX - window.pageXOffset, jsEvent.pageY - window.pageYOffset).id == 'btnDeleteEvent') return;
           $.ajax({
                url: '/tutee/events/' + event.id + '/details',
                success: function(response) {
                    $('#modalTitle').html(event.title);
                    $('#name').html(response.firstName + ' ' + response.lastName);
                    $('#time').html(event.start.format('HH:mm') + ' - ' + event.end.format('HH:mm'));
                    var subjects = response.subject1;
                    if (response.subject2) subjects += ', ' + response.subject2;
                    if (response.subject3) subjects += ', ' + response.subject3;
                    if (response.subject4) subjects += ', ' + response.subject4;
                    $('#subjects').html(subjects);
                    if(response.eventDescription) { $('#eventDescription').html(response.eventDescription); }
                        else { $('#eventDescription').html('None'); }
                    $('#fullCalModal').modal();

                },
                error: function(e) {
                    alert(e);
                }
            });
        },
        events: function(start, end, timezone, callback) {
            $.ajax({
                url: '/tutee/events.json',
                data: {
                    start: start.format("YYYY-MM-DD[T]HH:mm:ss"),
                    end: end.format("YYYY-MM-DD[T]HH:mm:ss")
                },
                success: function(response) {
                    callback(response);
                },
                error: function(e) {
                    alert(e);
                }
            });
        }
    });

});