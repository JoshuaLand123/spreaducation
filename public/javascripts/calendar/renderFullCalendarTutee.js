$(document).ready(function() {

/* initialize the external events
    -----------------------------------------------------------------*/

    $('#external-events .fc-event').each(function() {

      // store data so the calendar knows to render an event upon drop
      $(this).data('event', {
        title: 'temp', // use the element's text as the event title
        constraint: 'available',
        render: false,
        stick: true // maintain when user navigates (see docs on the renderEvent method)
      });

      // make the event draggable using jQuery UI
      $(this).draggable({
        zIndex: 999,
        revert: true,      // will cause the event to go back to its
        revertDuration: 0  //  original position after the drag
      });

    });
    var tutorID = $('#tutorID').html();
    $('#calendar').fullCalendar({
        height: 'auto',
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'agendaWeek,agendaDay,listMonth'
        },
        defaultView: 'agendaWeek',
        locale: 'de',
        navLinks: true, // can click day/week names to navigate views
        editable: true,
        eventDurationEditable: false,
        eventLimit: true,
        allDaySlot: false,
        droppable: true,
            drop: function(date) {
                if (confirm("Are you sure you want to change this event?")) {
                    var eventData = $(this).data('event');
                    eventData.title = 'Meeting Requested';
                    eventData.start = date.clone();
                    eventData.end = date.add(1, 'hours').add(30, 'minutes');
                    eventData.constraint = 'available';
                        $.ajax({
                              url: '/events/request',
                              data: {
                                  tutorID: tutorID,
                                  start: eventData.start.format(),
                                  end: eventData.end.format()
                              },
                              success: function(response) {
                                  eventData.id = response;
                                  $('#calendar').fullCalendar('renderEvent', eventData, true);
                                  eventData.title = 'temp';
                              },
                              error: function(e) {
                                  alert(e);
                              }
                        });
                }
            },
        slotDuration: '00:30:00',
        slotLabelFormat: 'H:mm',
        slotLabelInterval: '01:00:00',
        forceEventDuration: true,
        minTime: '08:00:00',
        maxTime: '22:00:00',
        defaultTimedEventDuration: '01:30:00',
        eventOverlap: function(stillEvent, movingEvent) {
            return stillEvent.rendering == "background" && stillEvent.id == "available";
        },
        eventDrop: function(event, delta, revertFunc) {
            if (!confirm("Are you sure you want to change this event?")) {
                revertFunc();
            } else {
                $.ajax({
                    url: '/events/' + event.id + '/update',
                    data: {
                        start: event.start.format(),
                        end: event.end.format()
                    },
                    success: function(response) {
                        $('#calendar').fullCalendar('updateEvent', event);
                    },
                    error: function(e) {
                        alert(e);
                        revertFunc();
                    }
                });
            }

        },
        eventRender: function(event, element) {
            if(event.title == 'temp') {
                $('#calendar').fullCalendar('removeEvents', event._id);
                return false;
            }
            if(event.rendering != 'background') {
                element.find(".fc-bg").css("pointer-events", "none");
                element.append("<div style='position:absolute;bottom:0px;right:0px' ><button type='button' id='btnDeleteEvent' class='btn btn-sm btn-block btn-primary btn-flat'>X</button></div>");
                element.find("#btnDeleteEvent").click(function() {
                    if (confirm("Are you sure you want to delete this event?")) {
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
        events: function(start, end, timezone, callback) {
            var start = start.format("YYYY-MM-DD[T]HH:mm:ss");
            var end = end.format("YYYY-MM-DD[T]HH:mm:ss");
            $.ajax({
                url: '/events/tutor',
                data: {
                    tutorID: tutorID,
                    start: start,
                    end: end
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