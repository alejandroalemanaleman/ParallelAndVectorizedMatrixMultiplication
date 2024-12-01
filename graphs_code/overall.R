vec <- vectorized %>%
  rename(mem_basic=memory_used_basic, mem_vectorized=memory_used_vectorized, exec_vectorized = execution_time_vectorized, exec_basic = execution_time_basic, `Matrix Size`=matriz_size)

overrall <- parallel %>%
    left_join(vec, by="Matrix Size")
overrall <- overrall %>%
  left_join(atomic, by = "Matrix Size")

overrall_ <- overrall %>%
  pivot_longer(c(exec_atomic, exec_basic.x, exec_fixed, exec_semaphore, exec_streams, exec_vectorized, exec_synchronized), names_to = "algorithm", values_to = "execution_time")

overrall_1 <- overrall_ %>%
  filter(`Threads`== 8)



ggplot(overrall_1, aes(x = `Matrix Size`, y = execution_time, color = as.factor(algorithm))) +
  geom_line(aes(group = algorithm)) +
  geom_point() +
  geom_text(data = overrall_1 %>% group_by(algorithm) %>% slice_max(`Matrix Size`),
            aes(label = algorithm),
            hjust = 1.1, size = 3) +
  labs(title = "Execution time by Matrix Size in all algorithms tested",
       x = "Matrix Size",
       y = "Execution time (ms)",
       color = "Algorithm") +
  theme_minimal() +
  theme(axis.text.y = element_text(color = "grey"))+ theme(legend.position = "bottom")

overrall__ <- overrall %>%
  pivot_longer(c(mem_atomic, mem_basic.x, mem_fixed, mem_semaphore, mem_streams, mem_vectorized, mem_synchronized), names_to = "algorithm", values_to = "memory_used")

overrall__1 <- overrall__ %>%
  filter(`Threads`== 8)


ggplot(overrall__1, aes(x = `Matrix Size`, y = memory_used, color = as.factor(algorithm))) +
  geom_line(aes(group = algorithm)) +
  geom_point() +
  geom_text(data = overrall__1 %>% group_by(algorithm) %>% slice_max(`Matrix Size`),
            aes(label = algorithm),
            hjust = 1.1, size = 3) +
  labs(title = "Memory use by Matrix Size in all algorithms tested",
       x = "Matrix Size",
       y = "Memory Use (KB)",
       color = "Algorithm") +
  theme_minimal() +
  theme(axis.text.y = element_text(color = "grey"))+ theme(legend.position = "bottom")
