# Distributed application using RabbitMQ
Distributed application to edit and send and receive text using asynchronous communication based on [RabbitMQ API](https://www.rabbitmq.com/).

The application was developed in an incremental iterative way.

- **1st iteration** : <br>
Creation of *3 programs*: two of which will **write text** in text zones and the third will receive and **display** what was written as explained in the following schema.

![Functionalities for the first iteration](/_1st_iteration/functionalities.png)

<br>
The code for the **two editing programs** is contained in Task 1 and Task2 while Task3 is the **display program**. The Send class will just encapsulate the details of sending messages to RabbitMQ. An execution of this application in this stage would like :

<p align="center">
   <img src="/_1st_iteration/execution_example.png" alt="Example of execution in the first iteration">
</p>

- **2nd iteration** : <br>
  - Since Task1 and Task2 can only write text (cannot edit or delete it), I added the **edit and delete options**.
  - I **generalized the solution for n editing Tasks** to write, edit and delete text instead of just two processes (Always only one display process). The number of the editor process (Task) is passed as an argument.

In this stage, the result of the execution of this application for 4 editing Tasks would louk like :
<p align="center">
   <img src="/_2nd_iteration/execution_example.png" alt="Example of execution in the second iteration">
</p>

- **3rd iteration** : <br>
  - **All** processes **can edit text** (and send it to other processes) and **display texts** from other processes. Each process will have a text box where it can write its own text and text boxes to display the texts of other processes, as explained in the following schema :

![Functionalities for the third iteration](/_3rd_iteration/functionalities.png)

  - I **generalized the solution for n processes** (the process number will be passed as an argument).

In this stage, the result of the execution of this application for 4 editing Tasks would louk like :
<p align="center">
   <img src="/_3rd_iteration/execution_example.png" alt="Example of execution in the third iteration">
</p>

- **4th iteration** : <br>
  - I made an **ergonomic text editing interface** (the **same text area for everyone**) in which we can write at the same time.

![Functionalities for the forth iteration](/_4th_iteration/functionalities.png)

The first attempt was a simplified interface :
<p align="center">
   <img src="/_4th_iteration/simple_gui/execution_example.png" alt="Example of execution in the forth iteration">
</p>

Which was improved later on :
<p align="center">
   <img src="/_4th_iteration/more_ergonomic_interface/exceution_example.png" alt="Example of execution in the forth iteration">
</p>

<br><br><br>

<p align="center">
   <img src="/_4th_iteration/more_ergonomic_interface/exceution_example_2.png" alt="Example of execution in the forth iteration">
</p>
