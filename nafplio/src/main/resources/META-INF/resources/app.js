const sidePanel = document.getElementById("side-panel");
const messageContainer = document.getElementById("message-container");
const userInput = document.getElementById("user-input");
const sendButton = document.getElementById("send-button");
const projectList = document.getElementById("project-list");

userInput.disabled = true;
sendButton.disabled = true;
sidePanel.disabled = true;

fetchExistingProjects();

function setActiveProject(selectedProject) {
    const activeProject = document.querySelector(".active-project");
    if (activeProject) {
        activeProject.classList.remove("active-project");
    }

    selectedProject.classList.add("active-project");
}

// Function to add messages to the DOM
function addMessage(content, isUser) {
    const message = document.createElement("div");
    message.classList.add("p-4", "rounded", "mb-1");
    message.classList.add(isUser ? "text-right" : "text-left");
    message.classList.add(isUser ? "bg-blue-100" : "bg-gray-200");
    message.innerText = content;

    return message;
}

function enableChatInput() {
    userInput.disabled = false;
    sendButton.disabled = false;
}

function enableSidePanel() {
    sidePanel.disabled = false;
}

function fetchExistingProjects() {
    const endpoint = `/project/get-all`;

    fetch(endpoint)
        .then((response) => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json(); // Parse response as JSON
        })
        .then((projects) => {
            projects.forEach((project) => addProject(project.nickname));

            enableSidePanel();
        })
        .catch((error) => {
            console.error("Error fetching response:", error);
        });
}

function addProject(name = `Project ${projectList.childElementCount + 1}`) {
    const projectItem = document.createElement("li");
    projectItem.classList.add("p-2", "bg-gray-100", "rounded", "shadow-sm");
    projectItem.textContent = name;

    projectItem.addEventListener("click", () => {
        console.log("click!")
        setActiveProject(projectItem);
    });

    projectList.appendChild(projectItem);

    enableChatInput();
}


// Function to send a message
function sendMessage() {
    const text = userInput.value.trim();
    if (!text) return;

    messageContainer.appendChild(addMessage(text, true));
    messageContainer.scrollTop = messageContainer.scrollHeight;
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
                .replaceAll(/data:/g, '').replaceAll(/\n/g, ''); // Extract the response

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