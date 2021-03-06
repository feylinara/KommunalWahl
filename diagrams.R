#!/usr/bin/env Rscript

library(ggplot2)
library(dplyr)

args <- commandArgs(trailingOnly = TRUE)
file  <- "output/ModelOutput.2019.Jan.11.11_31_22.txt"
params_file <- "output/ModelOutput.2019.Jan.11.11_31_22.batch_param_map.txt"

df <- read.csv(file)
df$wouldVote <- as.factor(df$wouldVote)

params <- read.csv(params_file)

visualise_file <- function(file, step) {
  aggregated <- df %>% filter(run == step) %>%
    group_by(tick, wouldVote) %>% tally() %>% ungroup()
  title <- params %>% filter(run == step) %>% select(partyInfluence, socialInfluence) %>% toString()
  p <- ggplot(aggregated, aes(tick, n, colour = wouldVote)) + geom_line() + labs(title=title)
  png_file <- paste(file, title, "png", sep=".")
  ggsave(png_file)
}

for (step in 1:max(df$run)){
  visualise_file(file, step)
  ## readline(prompt="Press [enter] to continue")
}
