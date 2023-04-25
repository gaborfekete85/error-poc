
//    @Entity
//    @Table(name = "task")
//    class Task {
//        @Id
//        @Column(name = "id")
//        private Long id;
//
//        @Column(name = "description", nullable = false, length = 200)
//        //@Length(min = 0, max = 200)
//        private String description;
//
//        @Column(name = "priority", nullable = false)
//        private Long priority;
//
//        public Long getId() {
//            return id;
//        }
//
//        public void setId(Long id) {
//            this.id = id;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//
//        public Long getPriority() {
//            return priority;
//        }
//
//        public void setPriority(Long priority) {
//            this.priority = priority;
//        }
//    }
//
//    @RestController
//    class TaskController {
//
//        @Autowired
//        private TaskRepository taskRepository;
//
//        @PutMapping("/tasks/{id}")
//        public ResponseEntity<Object> createTask(@PathVariable Long id, @RequestBody TaskDto task) {
//            if (task.getDescription() == null) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Task description is required", 400));
//            }
//
//            Optional<Task> foundTask = taskRepository.findById(id);
//
//            if (foundTask.isPresent()) {
//                Task taskEntity = foundTask.get();
//                taskEntity.setDescription(task.getDescription());
//                taskEntity.setPriority(task.getPriority());
//                Task createdTask = taskRepository.save(taskEntity);
//                return ResponseEntity.status(HttpStatus.OK).body(new TaskDto(createdTask.getDescription(), createdTask.getPriority()));
//            }
//
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Cannot find task with given id", 404));
//
//        }
//
//    }
//
//    class TaskDto {
//
//        private String description;
//
//        private Long priority;
//
//        public TaskDto() {
//        }
//
//        public TaskDto(String description, Long priority) {
//            this.description = description;
//            this.priority = priority;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//
//        public Long getPriority() {
//            return priority;
//        }
//
//        public void setPriority(Long priority) {
//            this.priority = priority;
//        }
//    }
//
//    class Response {
//        private String message;
//        private int status;
//
//        public Response() {
//        }
//
//        public Response(String message, int status) {
//            this.message = message;
//            this.status = status;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public int getStatus() {
//            return status;
//        }
//    }