$(document).ready(function() {

/* initialize the external events
    -----------------------------------------------------------------*/

    $('#external-events .fc-event').each(function() {

      // store data so the calendar knows to render an event upon drop
      $(this).data('event', {
        title: 'temp', // use the element's text as the event title
        constraint: 'available',
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
        nowIndicator: true,
        locale: 'de',
        navLinks: true, // can click day/week names to navigate views
        editable: true,
        eventDurationEditable: false,
        eventLimit: true,
        allDaySlot: false,
        droppable: true,
            drop: function(date) {
                var description = prompt('Bitte beschreibe was du in der Stunde lernen möchtest:');
                if (description) {
                    var eventData = $.extend({}, $(this).data('event'));
                    eventData.title = 'Requested';
                    eventData.start = date.clone();
                    eventData.end = date.clone().add(1.5, 'hours');
                    eventData.editable = true;
                        $.ajax({
                              url: '/tutee/events/request',
                              data: {
                                  tutorID: tutorID,
                                  start: eventData.start.format(),
                                  end: eventData.end.format(),
                                  description: description
                              },
                              success: function(response) {
                                  eventData.id = response;
                                  $('#calendar').fullCalendar('renderEvent', eventData, true);
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
            if (!confirm("Bist du sicher, dass du dieses Ereignis ändern willst?")) {
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
                        revertFunc();
                        alert(e);
                    }
                });
            }

        },
        eventRender: function(event, element) {
            if(event.title == 'temp') {
                $('#calendar').fullCalendar('removeEvents', event._id);
                return false;
            }
            if (event.editable) {
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
           if (document.elementFromPoint(jsEvent.pageX, jsEvent.pageY).id == 'btnDeleteEvent') return;
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
                url: '/tutee/tutorEvents.json',
                data: {
                    tutorID: tutorID,
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