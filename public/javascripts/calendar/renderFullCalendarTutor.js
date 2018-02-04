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
        editable: true,
        eventLimit: true,
        allDaySlot: false,
        selectable: true,
        selectOverlap: false,
        selectHelper: true,
        slotDuration: '00:30:00',
        slotLabelFormat: 'H:mm',
        slotLabelInterval: '01:00:00',
        minTime: '08:00:00',
        maxTime: '22:00:00',
        defaultTimedEventDuration: '01:30:00',
        forceEventDuration: true,
        eventRender: function(event, element) {
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
        eventOverlap: false,
        eventResize: function(event, delta, revertFunc) {
            if (!confirm("Bist du sicher, dass du dieses Ereignis ändern willst?")) {
                revertFunc();
            } else {
                var start = event.start;
                var end = event.end;
                if (moment.duration(end.diff(start)).asHours() < 1.5) {
                    end = start.clone().add(1.5, 'hours');
                    event.end = end;
                }
                $.ajax({
                    url: '/events/' + event.id + '/update',
                    data: {
                        start: start.format(),
                        end: end.format()
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
                    type: 'GET',
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
        select: function(start, end) {
            var isValid = true;
            if (moment.duration(end.diff(start)).asHours() < 1.5) {
                end = start.clone().add(1.5, 'hours');
                isValid = $.grep($('#calendar').fullCalendar('clientEvents'), function (v) {
                    return v.start < end && v.end > end;
                }).length == 0;
            }
            if (isValid) {
                $.ajax({
                    url: '/tutor/events/addAvailability',
                    data: {
                        start: start.format("YYYY-MM-DD[T]HH:mm:ss"),
                        end: end.format("YYYY-MM-DD[T]HH:mm:ss")
                    },
                    success: function(response) {
                        var eventData = {
                            id: response,
                            title: 'Available',
                            start: start,
                            end: end,
                            editable: true,
                            color: '#7dd322'
                        };
                        $('#calendar').fullCalendar('renderEvent', eventData, true); // stick? = true
                    },
                    error: function(e) {
                        alert(e);
                    }
                });
            }
            $('#calendar').fullCalendar('unselect');
        },
        eventClick: function(event, jsEvent) {
           if (event.title == 'Available' || document.elementFromPoint(jsEvent.pageX - window.pageXOffset, jsEvent.pageY - window.pageYOffset).id == 'btnDeleteEvent') return;
           $.ajax({
                url: '/tutor/events/' + event.id + '/details',
                success: function(response) {
                    $('#modalTitle').html(event.title);
                    $('#name').html(response.firstName + ' ' + response.lastName);
                    $('#time').html(event.start.format('HH:mm') + ' - ' + event.end.format('HH:mm'));
                    $('#subjects').html(response.subjectImprove1 + ', ' + response.subjectImprove2);
                    if (response.eventDescription) { $('#eventDescription').html(response.eventDescription);
                    } else { $('#eventDescription').html('None'); }

                    if (response.eventType == 'Requested') { $('#buttons-request').show(); $('#buttons-cancel').hide(); }
                        else { $('#buttons-request').hide(); $('#buttons-cancel').show(); }
                     $('#confirm').unbind("click").click(function() {
                        $.ajax({
                            url: '/tutor/events/' + event.id + '/confirm',
                            success: function(response) {
                                event.color = 'green';
                                event.title = 'Confirmed';
                                $('#calendar').fullCalendar('updateEvent', event);
                            },
                            error: function(e) {
                                alert(e);
                            }
                        });
                     });
                     $('#decline').unbind("click").click(function() {
                         var reason = prompt("Bitte gebe deine Gründe an:");
                         if (reason) {
                            $.ajax({
                                url: '/tutor/events/' + event.id + '/decline',
                                data: {
                                    reason: reason
                                },
                                success: function(response) {
                                    $('#calendar').fullCalendar('removeEvents', event._id);
                                },
                                error: function(e) {
                                    alert(e);
                                }
                            });
                            }
                     });

                    $('#fullCalModal').modal();

                },
                error: function(e) {
                    alert(e);
                }
            });
        },
        events: function(start, end, timezone, callback) {
            $.ajax({
                url: '/tutor/events.json',
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