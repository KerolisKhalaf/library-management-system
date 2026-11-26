#!/bin/sh
set -eu

Xvfb "$DISPLAY" -screen 0 1280x800x24 -ac +extension GLX +render -noreset &
xvfb_pid=$!

cleanup() {
    kill "$app_pid" "$websockify_pid" "$vnc_pid" "$window_manager_pid" "$xvfb_pid" 2>/dev/null || true
}

app_pid=
websockify_pid=
vnc_pid=
window_manager_pid=
trap cleanup EXIT INT TERM

mkdir -p "$LIBRARY_DATA_DIR"

until [ -S /tmp/.X11-unix/X0 ]; do
    if ! kill -0 "$xvfb_pid" 2>/dev/null; then
        echo "Xvfb failed to start" >&2
        exit 1
    fi
    sleep 0.1
done

fluxbox &
window_manager_pid=$!
x11vnc -display "$DISPLAY" -forever -shared -nopw -localhost -rfbport 5900 &
vnc_pid=$!
websockify --web=/usr/share/novnc 6080 localhost:5900 &
websockify_pid=$!

cd /opt/app
mvn -B -o javafx:run &
app_pid=$!
wait "$app_pid"
