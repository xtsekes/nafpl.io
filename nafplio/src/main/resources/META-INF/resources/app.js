const sidePanel = document.getElementById("side-panel");
const messageContainer = document.getElementById("message-container");
const userInput = document.getElementById("user-input");
const sendButton = document.getElementById("send-button");
const projectList = document.getElementById("project-list");

document.getElementById("add-project-btn").addEventListener("click", openAddProjectModal);

userInput.disabled = true;
sendButton.disabled = true;
sidePanel.disabled = true;

let selectedProjectNickname = null;

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
            return response.json();
        })
        .then((projects) => {
            projects.forEach((project) => addProject(project.nickname, false));

            enableSidePanel();
        })
        .catch((error) => {
            console.error("Error fetching response:", error);
        });
}

function fetchProjectById(projectNickname) {
    const endpoint = `/project/get-project/${projectNickname}`;

    fetch(endpoint)
        .then((response) => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then((project) => {
            selectedProjectNickname = project.nickname;
            console.log(project);
            console.log(selectedProjectNickname);
        })
        .catch((error) => {
            console.error("Error fetching response:", error);
        });
}

function addProject(name, manualAdd) {
    const projectItem = document.createElement("li");
    projectItem.classList.add("p-2", "bg-gray-100", "rounded", "shadow-sm");
    projectItem.textContent = name;

    projectItem.addEventListener("click", () => {
        setActiveProject(projectItem);
        fetchProjectById(projectItem.textContent);
    });

    projectList.appendChild(projectItem);

    if (manualAdd) {
        setActiveProject(projectItem);
        projectItem.scrollIntoView({ behavior: "smooth", block: "center" });
    }

    enableChatInput();
}


// Function to send a message
function sendMessage() {
    const text = userInput.value.trim();
    if (!text) return;

    messageContainer.appendChild(addMessage(text, true));
    messageContainer.scrollTop = messageContainer.scrollHeight;
    userInput.value = "";

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

            let decodedText = decoder.decode(value, { stream: true });

            const matches = decodedText.matchAll(/^data:(.*)$/gm);

            while (true) {
                const match = matches.next();

                if(match.done) {
                    break;
                }

                responseText +=  match.value[1];
            }

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

function openAddProjectModal() {
    document.getElementById("add-project-modal").classList.add("active");
}

function closeAddProjectModal() {
    document.getElementById("add-project-modal").classList.remove("active");
    clearModalInputs();
}

function clearModalInputs() {
    document.getElementById("project-nickname").value = "";
    document.getElementById("project-path").value = "";
}

function submitProject() {
    const endpoint = `/project/create-project`;
    const nickname = document.getElementById("project-nickname").value.trim();
    const path = document.getElementById("project-path").value.trim();

    if (!nickname || !path) {
        alert("Please enter both a nickname and directory path.");
        return;
    }

    const data = {
        nickname: nickname,
        rootDirectory: path
    };

    fetch(endpoint, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.status !== 201) {
                throw new Error(`Failed to add project: Received status ${response.status}`);
            }

            return response.text().then(text => {
                return text ? JSON.parse(text) : {};
            });
        })
        .then(() => {
            addProject(data.nickname, true);
            closeAddProjectModal();
        })
        .catch(error => {
            console.error("Error adding project:", error);
            alert("Failed to add project. Please try again.");
        });
}

