const SERVER_URL = 'http://localhost:8080';

//welcome text after user logs in
const welcometext = document.getElementById("wtext");
const email = localStorage.getItem('email');
welcometext.innerText = `Welcome ${email}`; 
let jwt = localStorage.getItem('token'); // hämta JWT från localStorage

//logs out eventlistener
document.getElementById('logoutButton').addEventListener('click', function() {
    jwt = null;
    localStorage.removeItem('token'); 
    localStorage.removeItem('email');
    alert('You have logged out.');
    window.location.href = './index.html';
});

//fetches messages eventlistener
document.getElementById('fetchDataButton').addEventListener('click', function() {
    console.log('Fetch data button klickad');
    fetchMessage();
});

document.getElementById('sendMessageButton').addEventListener('click', function() {
sendMessage();
});

function sendMessage() {
    const message = document.getElementById('messageInput').value;
    const email = localStorage.getItem('email');
    const jwt = localStorage.getItem('token');

    //check if there is a msg and a jwt
    if (message && jwt) {
        //message data
        const messageData = {
            message: message,
            email: email 
        };

        fetch(`${SERVER_URL}/api/message/save`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwt}`
            },
            body: JSON.stringify(messageData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to send: ' + response.statusText);
            }
            console.log('Response status:', response.status);
            console.log('Response status:', response);

            return response.json(); 
        })
        .then(data => {
            alert('Message sent: ' + data.message);
            document.getElementById('messageInput').value = ''; 
            //fetchMessage()
        })
        .catch(error => console.error('Error:', error));
    } else {
        alert('Please enter a message and ensure you are authenticated.');
    }
};



//fetching messages from databse
function fetchMessage() {
    const email = localStorage.getItem('email');
    jwt = localStorage.getItem('token');
    const messageText = document.getElementById("messageText");

    console.log('Current JWT:', jwt);
    console.log('User Email:', email);

    if (jwt && email) {
        console.log(`Fetching messages for ${email} with jwt ${jwt}`);

        fetch(`${SERVER_URL}/api/message/fetch/${email}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwt}`,
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            console.log("Respone from fetching", response)
            return response.json();
        })
        .then(data => {
            console.log("data from fetching", data)

            messageText.innerHTML = '';
            data.forEach(msg => {
                const msgElement = document.createElement("p");
                msgElement.innerText = msg;//`${msg.message}`;
                messageText.appendChild(msgElement);
            });
        })
        .catch(error => {
            alert('Error fetching message: ' + error.message);
            console.error('Error fetching messages:', error);
        });
    } else {
        alert('You are not authenticated. Please log in to get the JWT.');
    }
}

