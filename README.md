<h1>Description:</h1>
The backend part of the fitness program application is a key segment of the system that enables management of all relevant data and business logic related to fitness programs and user interactions.

Using Spring Boot, a set of RESTful services has been developed specifically designed for manipulating data related to fitness programs. These services allow users to add new programs, view existing ones, participate in programs, ask questions, and communicate with advisors via messages.

One of the key functionalities of the backend part is support for tracking user activity logs and monitoring their progress. Users can input various information about types of exercises, duration, intensity, and results, while the application provides a graphical representation of progress, including weight loss, over a certain period. Additionally, users have the option to download their activity log as a PDF document.

The backend part also consumes external services such as an RSS feed to display the latest news from the world of fitness. This integration allows users to stay informed and inspired in their fitness activities.

One of the key functionalities is the periodic sending of new programs to subscribed users via email, providing them with regular updates and new exercise opportunities.

For additional security and protection of user data, Spring Security has been implemented, enabling user authentication via JWT tokens. This authentication allows secure access and usage of the application, while Spring authorization ensures that users have access only to functionalities for which they are authorized.
