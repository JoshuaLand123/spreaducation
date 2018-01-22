$(document).ready(function() {

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
        eventLimit: true,
        allDaySlot: false,
        selectable: true,
        selectOverlap: false,
        selectHelper: true,
        slotDuration: '00:30:00',
        slotLabelFormat: 'H:mm',
        slotLabelInterval: '01:00:00',
        forceEventDuration: true,
        minTime: '08:00:00',
        maxTime: '22:00:00',
        defaultTimedEventDuration: '01:30:00',
        eventRender: function(event, element) {
            element.find(".fc-bg").css("pointer-events", "none");
            element.append("<div style='position:absolute;bottom:0px;right:0px' ><button type='button' id='btnDeleteEvent' class='btn btn-sm btn-block btn-primary btn-flat'>X</button></div>");
            element.find("#btnDeleteEvent").click(function() {
                if (confirm("Are you sure you want to delete this event?")) {
                    $.ajax({
                        url: '/events/' + event.id + '/delete',
                        type: 'GET',
                        success: function(response) {
                            $('#calendar').fullCalendar('removeEvents', event._id);
                        },
                        error: function(e) {
                            alert(e);
                        }
                    });
                }

            });
        },
        eventOverlap: function(stillEvent, movingEvent) {
            return stillEvent.rendering == "background";
        },
        eventResize: function(event, delta, revertFunc) {
            if (!confirm("Are you sure you want to change this event?")) {
                revertFunc();
            } else {
                $.ajax({
                    url: '/events/' + event.id + '/update?start=' + event.start.format() + '&end=' + event.end.format(),
                    type: 'GET',
                    success: function(response) {
                        event.id = response;
                        $('#calendar').fullCalendar('updateEvent', event);
                    },
                    error: function(e) {
                        alert(e);
                        revertFunc();
                    }
                });
            }

        },
        eventDrop: function(event, delta, revertFunc) {
            if (!confirm("Are you sure you want to change this event?")) {
                revertFunc();
            } else {
                $.ajax({
                    url: '/events/' + event.id + '/update?start=' + event.start.format() + '&end=' + event.end.format(),
                    type: 'GET',
                    success: function(response) {
                        event.id = response;
                        $('#calendar').fullCalendar('updateEvent', event);
                    },
                    error: function(e) {
                        alert(e);
                        revertFunc();
                    }
                });
            }

        },
        select: function(start, end) {
            var start = start.format("YYYY-MM-DD[T]HH:mm:ss");
            var end = end.format("YYYY-MM-DD[T]HH:mm:ss");
            $.ajax({
                url: '/saveEvent?start=' + start + '&end=' + end,
                type: 'GET',
                success: function(response) {
                    var eventData = {
                        id: response,
                        title: 'Available',
                        start: start,
                        end: end
                    };
                    $('#calendar').fullCalendar('renderEvent', eventData, true); // stick? = true
                    $('#calendar').fullCalendar('unselect');
                },
                error: function(e) {
                    alert(e);
                }
            });
        },
        events: function(start, end, timezone, callback) {
            $.ajax({
                url: '/events',
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