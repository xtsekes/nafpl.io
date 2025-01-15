(function () {
    window.TheExperts = window.TheExperts || (window.TheExperts = {});
})();

(function () {
    /**
     * Enum for connection status
     * @readonly
     * @enum {{name: string, hex: string}}
     */
    const ConnectionStatus = Object.freeze({
        disconnected: Symbol("disconnected"),
        connecting: Symbol("connecting"),
        connected: Symbol("connected")
    });

    /**
     * @typedef {function} OnOpen
     * @param {SocketConnection} connection
     */

    /**
     * @typedef {function} OnClose
     * @param {SocketConnection} connection
     */

    /**
     * @typedef {function} OnMessageReceived
     * @param {SocketConnection} connection
     * @param {any} data
     */

    /**
     * A number, or a string containing a number.
     * @typedef {Object} Definition
     * @property {string} endpoint - The endpoint to connect to
     * @property {OnOpen} [onopen] - The function to call when the connection is opened
     * @property {OnClose} [onclose] - The function to call when the connection is closed
     * @property {OnMessageReceived} [onmessage] - The function to call when a message is received
     */

    /**
     * SocketConnection class
     * @class
     * @param {Definition} definition
     */
    const SocketConnection = function (definition) {
        this.status = ConnectionStatus.disconnected;
        this.definition = definition;
    }

    SocketConnection.prototype.connect = function () {
        const self = this;

        if (self.status !== ConnectionStatus.disconnected) {
            return;
        }

        self.status = ConnectionStatus.connecting;

        self.socket = new WebSocket(self.definition.endpoint);

        self.socket.onopen = function (evt) {

            self.status = ConnectionStatus.connected;

            if (!!self.definition && !!self.definition.onopen && typeof self.definition.onopen === 'function') {
                try {
                    self.definition.onopen(self);
                } catch (e) {
                    console.error(e);
                }
            }
        };

        self.socket.onclose = function (evt) {

            self.status = ConnectionStatus.disconnected;

            if (!!self.definition && !!self.definition.onclose && typeof self.definition.onclose === 'function') {

                try {
                    self.definition.onclose(self);
                } catch (e) {
                    console.error(e);
                }
            }

        };

        if (!!self.definition && !!self.definition.onmessage && typeof self.definition.onmessage === 'function') {
            self.socket.onmessage = function (evt) {
                try {
                    self.definition.onmessage(self, evt.data);
                } catch (e) {
                    console.error(e);
                }
            };
        }
    }

    SocketConnection.prototype.disconnect = function () {
        const self = this;

        if (self.status === ConnectionStatus.disconnected) {
            return;
        }

        self.socket.close();
    }

    /**
     * Send a message
     * @param {string | ArrayBufferLike | Blob | ArrayBufferView} data The first argument
     */
    SocketConnection.prototype.send = function (data) {
        const self = this;

        if (self.status !== ConnectionStatus.connected) {
            throw new Error("Connection is not open");
        }

        self.socket.send(data);
    }

    SocketConnection.prototype.destroy = function () {
        if (!!self.socket) {
            self.socket.disconnect();
            self.socket.destroy();
            self.socket = undefined;
        }
    }

    /**
     * @property {ConnectionStatus} Status
     * @property {SocketConnection} Connection
     */
    const websockets = {
        Status: ConnectionStatus,
        Connection: SocketConnection,
    };

    TheExperts.websockets = websockets;

})(TheExperts);
