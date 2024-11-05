const messageContainer = document.getElementById("message-container");
const userInput = document.getElementById("user-input");

// Function to add messages to the DOM
function addMessage(content, isUser) {
    const message = document.createElement("div");
    message.classList.add("p-4", "rounded", "mb-1");
    message.classList.add(isUser ? "text-right" : "text-left");
    message.classList.add(isUser ? "bg-blue-100" : "bg-gray-200");
    message.innerText = content;

    return message;

}

// Function to send a message
function sendMessage() {
    const text = userInput.value.trim();
    if (!text) return;

    //addMessage(text, true); // Display user's message
    messageContainer.appendChild(addMessage(text, true));
    messageContainer.scrollTop = messageContainer.scrollHeight; // Auto-scroll to the bottom
    userInput.value = "";   // Clear input

    // Start receiving the response
    streamResponse(text);
}

// Function to handle streaming response from the server
async function streamResponse(userMessage) {
    let responseText = ""; // To store the full response as chunks arrive
    let aiMessageElement = null; // Placeholder for the message element to update

    try {
        const endpoint = `/chat?prompt=${encodeURIComponent(userMessage)}`;
        const response = await fetch(endpoint);

        if (!response.ok) throw new Error("Network response was not ok");

        const reader = response.body.getReader();
        const decoder = new TextDecoder();

        // Read the response stream
        while (true) {
            const { done, value } = await reader.read();
            if (done) break;

            responseText += decoder.decode(value, { stream: true })
                .replace(/^data:/, '').replace(/\n/g, ''); // Extract the response

            // Only create the message element for the AI response on the first chunk
            if (!aiMessageElement) {
                aiMessageElement = messageContainer.appendChild(addMessage(responseText, false)); // Create and add to DOM
                messageContainer.scrollTop = messageContainer.scrollHeight;
            } else {
                aiMessageElement.innerText = responseText; // Update with new content
            }

            messageContainer.scrollTop = messageContainer.scrollHeight; // Keep scrolling to the bottom
        }
    } catch (error) {
        console.error("Error fetching response:", error);
        addMessage("Error fetching response. Please try again.", false);
    }
}