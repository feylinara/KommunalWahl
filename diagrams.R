#!/usr/bin/env Rscript

library(ggplot2)
library(dplyr)

args <- commandArgs(trailingOnly = TRUE)
file  <- args[1]

df <- read.csv(file)
df$wouldVote <- as.factor(df$wouldVote)

aggregated <- df %>%
    group_by(tick, wouldVote) %>% tally() %>% ungroup()

ggplot(aggregated, aes(tick, n, colour = wouldVote)) + geom_line()
